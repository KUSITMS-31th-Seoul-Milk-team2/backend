package com.seoulmilk.receipt.application;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.seoulmilk.receipt.exception.ReceiptErrorCode;
import com.seoulmilk.receipt.infrastructure.OAuth2TokenProvider;
import com.seoulmilk.receipt.infrastructure.mono.WebClientMonoUtil;
import com.seoulmilk.receipt.infrastructure.webclient.TaxReceiptWebClientUtil;
import com.seoulmilk.receipt.presentation.dto.request.TaxReceiptValidationRequest;
import com.seoulmilk.receipt.presentation.dto.request.TaxReceiptValidationWithAuthRequest;
import com.seoulmilk.receipt.presentation.dto.response.AdditionalAuthResponse;
import com.seoulmilk.receipt.presentation.dto.response.OAuth2TokenResponse;
import com.seoulmilk.receipt.presentation.dto.response.TaxReceiptValidationResponse;
import io.codef.api.EasyCodef;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import reactor.core.scheduler.Schedulers;

import java.io.UnsupportedEncodingException;
import java.time.Duration;
import java.time.Instant;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.atomic.AtomicReference;

@Service
@RequiredArgsConstructor
@Log4j2
public class ASyncTaxReceiptValidationService {
    private final OAuth2TokenProvider oAuth2TokenProvider;
    private final TaxReceiptWebClientUtil taxReceiptWebClientUtil;
    private final WebClientMonoUtil webClientMonoUtil;
    private final ObjectMapper objectMapper;

    // 토큰 캐싱을 위함 -> expires가 1주일로 설정되어 있음
    private final AtomicReference<Mono<String>> cachedToken = new AtomicReference<>();
    private final AtomicReference<Instant> tokenExpiry = new AtomicReference<>(Instant.MIN);

    // 캐싱 구현을 열심히 구현해봤지만... 레디스 도입이 시급해 보입니다...
    private Mono<String> getOAuth2TokenMono(){
        Instant now = Instant.now();
        if(cachedToken.get() != null || now.isAfter(tokenExpiry.get())){
            log.info("[getOAuth2TokenMono] 새로운 CODEF API OAuth2Token 요청");

            Mono<String> tokenMono = requestNewToken()
                    .cache()
                    .doOnNext(token -> log.info("[getOAuth2TokenMono] 새로운 토큰 발급 및 캐싱 완료"));

            cachedToken.set(tokenMono);
        }

        return cachedToken.get();
    }

    /**
     * OAuth2 서버에 새로운 토큰을 요청합니다
     * @return accessToken
     */
    private Mono<String> requestNewToken() {
        Map<String, String> headers = Map.of(
                "Content-Type", "application/x-www-form-urlencoded",
                "Authorization", oAuth2TokenProvider.createAuthHeader()
        );

        return Mono.fromCallable(() -> taxReceiptWebClientUtil.post(
                        oAuth2TokenProvider.getOAuth2Url(),
                        headers,
                        "grant_type=client_credentials&scope=read"
                ))
                .subscribeOn(Schedulers.boundedElastic())
                .handle((response, sink) -> {
                    try {
                        OAuth2TokenResponse tokenResponse = objectMapper.readValue(response, OAuth2TokenResponse.class);
                        Instant expiryTime = Instant.now().plusSeconds(tokenResponse.expiresIn() - 10); // 만료 10초 전까지만 사용
                        tokenExpiry.set(expiryTime);
                        log.info("[getOAuth2TokenMono] 토큰 발급 완료, 만료 시간: {}", expiryTime);
                        sink.next(tokenResponse.accessToken());
                    } catch (Exception e) {
                        log.error("[getOAuth2TokenMono] OAuth2Token 발급 실패 - 사유: {}", e.getMessage());
                        sink.error(ReceiptErrorCode.OAUTH2_TOKEN_ERROR.toException());
                    }
                });
    }

    public Mono<List<AdditionalAuthResponse>> multipleRecieptValidation(List<TaxReceiptValidationRequest> requests){
        return Flux.fromIterable(requests)
                .delayElements(Duration.ofMillis(500))  // 0.5초 간격
                .flatMap(request -> getOAuth2TokenMono()
                        .flatMap(token ->{
                            Map<String, Object> requestBody = objectMapper.convertValue(request, Map.class);
                            log.info("[multipleRecieptValidation] 요청 시작");
                            log.info("[multipleRecieptValidation] 보낸 데이터 - {}", requestBody);
                            return webClientMonoUtil.post(
                                            oAuth2TokenProvider.getTaxReceiptUrl(),
                                            createAuthHeaders(token),
                                            requestBody
                                    )
                                    .flatMap(response -> {
                                        log.info("[multipleRecieptValidation] 요청 완료");

                                        Map<String, Object> responseMap =
                                                taxReceiptWebClientUtil.decodeResponse(response, "multipleRecieptValidation");
                                        return Mono.just(objectMapper.convertValue(responseMap.get("data"), AdditionalAuthResponse.class));
                                    });
                        })
                )
                .collectList();  // 리스트로 반환
    }

    public List<TaxReceiptValidationResponse> multipleValidationWithAuth(
            List<TaxReceiptValidationWithAuthRequest> requests
    ){
//        List<TaxReceiptValidationResponse> responses = new ArrayList<>();
//
//        EasyCodef easyCodef = new EasyCodef();
//        easyCodef.setPublicKey(oAuth2TokenProvider.getPublicKey());
//        easyCodef.setClientInfoForDemo(oAuth2TokenProvider.getClientId(), oAuth2TokenProvider.getClientSecret());
//        String endPoint = "/v1/kr/public/nt/third-party/tax-invoice-issue";
//
//        for(TaxReceiptValidationWithAuthRequest request : requests){
//            HashMap<String, Object> requestBody = objectMapper.convertValue(request, HashMap.class);
//
//            log.info("[getAdditionalAuthResponses] 보낸 데이터 - {}", requestBody);
//            String response = null;
//            try {
//                response = easyCodef.requestCertification(endPoint, EasyCodefServiceType.DEMO, requestBody);
//            } catch (UnsupportedEncodingException e) {
//                throw new RuntimeException(e);
//            } catch (JsonProcessingException e) {
//                throw new RuntimeException(e);
//            } catch (InterruptedException e) {
//                throw new RuntimeException(e);
//            }
//            log.info("[getAdditionalAuthResponses] 받은 데이터 - {}", response);
//            Map<String, Object> decodedResponse = taxReceiptWebClientUtil.decodeResponse(
//                    response,
//                    "getAdditionalAuthResponses"
//            );
//
//            objectMapper.convertValue(decodedResponse.get("data"), TaxReceiptValidationResponse.class);
//        }
//        return responses;
        return null;
    }

    private Map<String, String> createAuthHeaders(String token){
        return Map.of(
                "Authorization", "Bearer " + token,
                "Content-Type", MediaType.APPLICATION_JSON_VALUE
        );
    }
}
