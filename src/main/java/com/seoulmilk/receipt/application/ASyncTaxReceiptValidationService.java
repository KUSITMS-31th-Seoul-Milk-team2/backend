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

import java.time.Duration;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@Service
@RequiredArgsConstructor
@Log4j2
public class ASyncTaxReceiptValidationService {
    private final OAuth2TokenProvider oAuth2TokenProvider;
    private final TaxReceiptWebClientUtil taxReceiptWebClientUtil;
    private final WebClientMonoUtil webClientMonoUtil;
    private final ObjectMapper objectMapper;

    private String accessToken;
    /**
     * CODEF OPEN API를 사용하기 위한 OAuth2 토큰을 발급 받습니다
     * @return OAuth2TokenResponse
     */
    private OAuth2TokenResponse getOAuth2Token(){
        log.info("[getOAuth2Token] CODEF OPEN API OAuth2Token 발급 시작");

        Map<String, String> headers = Map.of(
                "Content-Type", "application/x-www-form-urlencoded",
                "Authorization", oAuth2TokenProvider.createAuthHeader()
        );

        // 아직 redis 도입 이전이므로 추후 access token 저장 방안 고민
        String response = taxReceiptWebClientUtil.post(
                oAuth2TokenProvider.getOAuth2Url(),
                headers,
                "grant_type=client_credentials&scope=read"
        );

        try {
            log.info("[getOAuth2Token] CODEF OPEN API OAuth2Token 발급 완료");
            return objectMapper.readValue(response, OAuth2TokenResponse.class);
        } catch (Exception e) {
            log.error("[getOAuth2Token] CODEF OPEN API OAuth2Token 발급 실패 - 사유 : {}", e.getMessage());
            throw ReceiptErrorCode.OAUTH2_TOKEN_ERROR.toException();
        }
    }


    public Mono<List<AdditionalAuthResponse>> multipleRecieptValidation(List<TaxReceiptValidationRequest> requests){
        OAuth2TokenResponse oAuth2TokenResponse = getOAuth2Token();
        String accessToken = oAuth2TokenResponse.accessToken();

        // 비동기 요청 처리 리스트
        List<Mono<AdditionalAuthResponse>> requestMonos = new ArrayList<>();

        for (TaxReceiptValidationRequest taxReceiptValidationRequest : requests) {
            // 객체 -> Map 변환
            Map<String, Object> requestBody = objectMapper.convertValue(taxReceiptValidationRequest, Map.class);

            // 비동기 요청을 리스트에 추가
            Mono<AdditionalAuthResponse> requestMono = webClientMonoUtil.post(
                            oAuth2TokenProvider.getTaxReceiptUrl(),
                            createAuthHeaders(),
                            requestBody
                    )
                    .flatMap(response -> {
                        Map<String, Object> responseMap =
                                taxReceiptWebClientUtil.decodeResponse(response, "multipleRecieptValidation");
                        return Mono.just(objectMapper.convertValue(responseMap.get("data"), AdditionalAuthResponse.class));
                    });

            requestMonos.add(requestMono);
        }

        return Flux.fromIterable(requestMonos)
                .concatMap(request -> request.delayElement(Duration.ofMillis(500)))
                .collectList();
    }

    public Mono<List<TaxReceiptValidationResponse>> multipleValidationWithAuth(
            List<TaxReceiptValidationWithAuthRequest> requests
    ){
        List<Mono<TaxReceiptValidationResponse>> requestMonos = new ArrayList<>();

        for(TaxReceiptValidationWithAuthRequest taxReceiptValidationWithAuthRequest : requests){
            Map<String, Object> requestBody =
                    objectMapper.convertValue(taxReceiptValidationWithAuthRequest, Map.class);

            log.info("[multipleValidationWithAuth] 추가 인증 데이터를 포함한 세금계산서 다중 검증 시작");
            Mono<TaxReceiptValidationResponse> requestMono = webClientMonoUtil.post(
                    oAuth2TokenProvider.getTaxReceiptUrl(),
                    createAuthHeaders(),
                    requestBody
            ).flatMap(response -> {
                Map<String, Object> responseMap =
                        taxReceiptWebClientUtil.decodeResponse(response, "multipleValidationWithAuth");
                return Mono.just(objectMapper.convertValue(responseMap.get("data"), TaxReceiptValidationResponse.class));
            });

            requestMonos.add(requestMono);
        }

        return Flux.fromIterable(requestMonos)
                .concatMap(request -> request.delayElement(Duration.ofMillis(500)))
                .collectList();
    }

    private Map<String, String> createAuthHeaders(){
        return Map.of(
                "Authorization", "Bearer " + accessToken,
                "Content-Type", MediaType.APPLICATION_JSON_VALUE
        );
    }
}
