package com.seoulmilk.receipt.application;

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
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import reactor.core.scheduler.Schedulers;

import java.time.Duration;
import java.time.Instant;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.atomic.AtomicReference;
import java.util.stream.Collectors;

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
                                        long endTime = System.currentTimeMillis();
                                        log.info("[multipleRecieptValidation] 요청 완료");

                                        Map<String, Object> responseMap =
                                                taxReceiptWebClientUtil.decodeResponse(response, "multipleRecieptValidation");
                                        return Mono.just(objectMapper.convertValue(responseMap.get("data"), AdditionalAuthResponse.class));
                                    });
                        })
                )
                .collectList();
    }

    public Mono<List<TaxReceiptValidationResponse>> multipleValidationWithAuth(
            List<TaxReceiptValidationWithAuthRequest> requests
    ){
            return Flux.fromIterable(requests)
                .delayElements(Duration.ofMillis(500)) // 0.5초 간격으로 요청 전송
                .flatMap(request -> getOAuth2TokenMono()
                        .flatMap(token -> {
                            Map<String, Object> requestBody = objectMapper.convertValue(request, Map.class);
                            log.info("[multipleValidationWithAuth] 추가 인증 데이터를 포함한 세금계산서 다중 검증 시작");
                            log.info("[multipleValidationWithAuth] 보낸 데이터 - {}", requestBody);
                            return webClientMonoUtil.post(
                                            oAuth2TokenProvider.getTaxReceiptUrl(),
                                            createAuthHeaders(token),
                                            requestBody
                                    )
                                    .flatMap(response -> {
                                        long endTime = System.currentTimeMillis();
                                        log.info("[multipleValidationWithAuth] 요청 완료");

                                        Map<String, Object> responseMap =
                                                taxReceiptWebClientUtil.decodeResponse(response, "multipleValidationWithAuth");
                                        return Mono.just(objectMapper.convertValue(responseMap.get("data"), TaxReceiptValidationResponse.class));
                                    });
                        })
                )
                .collectList();
    }

    private Map<String, String> createAuthHeaders(String token){
        return Map.of(
                "Authorization", "Bearer " + token,
                "Content-Type", MediaType.APPLICATION_JSON_VALUE
        );
    }
}
