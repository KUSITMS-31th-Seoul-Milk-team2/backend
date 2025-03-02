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
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Log4j2
public class ASyncTaxReceiptValidationService {
    private final OAuth2TokenProvider oAuth2TokenProvider;
    private final TaxReceiptWebClientUtil taxReceiptWebClientUtil;
    private final WebClientMonoUtil webClientMonoUtil;
    private final ObjectMapper objectMapper;

    /**
     * CODEF OPEN API를 사용하기 위한 OAuth2 토큰을 비동기 방식으로 발급 받습니다
     * @return Mono<OAuth2TokenResponse>
     */
    private Mono<String> getOAuth2TokenMono(){
        log.info("[getOAuth2TokenMono] CODEF OPEN API OAuth2Token 발급 시작");

        Map<String, String> headers = Map.of(
                "Content-Type", "application/x-www-form-urlencoded",
                "Authorization", oAuth2TokenProvider.createAuthHeader()
        );

        return Mono.fromCallable(() -> taxReceiptWebClientUtil.post(
                        oAuth2TokenProvider.getOAuth2Url(),
                        headers,
                        "grant_type=client_credentials&scope=read"
                ))
                .map(response -> {
                    try {
                        log.info("[getOAuth2TokenMono] CODEF OPEN API OAuth2Token 발급 완료");
                        OAuth2TokenResponse tokenResponse = objectMapper.readValue(response, OAuth2TokenResponse.class);
                        return tokenResponse.accessToken();
                    } catch (Exception e) {
                        log.error("[getOAuth2TokenMono] OAuth2Token 발급 실패 - 사유: {}", e.getMessage());
                        throw ReceiptErrorCode.OAUTH2_TOKEN_ERROR.toException();
                    }
                });
    }

    public Mono<List<AdditionalAuthResponse>> multipleRecieptValidation(List<TaxReceiptValidationRequest> requests){
        // 각 요청마다 새로운 토큰을 가져와서 사용
        List<Mono<AdditionalAuthResponse>> requestMonos = requests.stream()
                .map(request -> getOAuth2TokenMono() // 매 요청마다 새로운 토큰 가져오기
                        .flatMap(token -> {
                            // 요청을 보낼 데이터 변환
                            Map<String, Object> requestBody = objectMapper.convertValue(request, Map.class);
                            log.info("[multipleRecieptValidation] 데이터 확인 - {}", requestBody);

                            return webClientMonoUtil.post(
                                            oAuth2TokenProvider.getTaxReceiptUrl(),
                                            createAuthHeaders(token), // 새 토큰 적용
                                            requestBody
                                    )
                                    .flatMap(response -> {
                                        Map<String, Object> responseMap =
                                                taxReceiptWebClientUtil.decodeResponse(response, "multipleRecieptValidation");
                                        return Mono.just(objectMapper.convertValue(responseMap.get("data"), AdditionalAuthResponse.class));
                                    });
                        })
                )
                .collect(Collectors.toList());

        // 모든 요청을 병렬 실행하고 결과를 리스트로 반환
        return Flux.merge(requestMonos).collectList();
    }

    public Mono<List<TaxReceiptValidationResponse>> multipleValidationWithAuth(
            List<TaxReceiptValidationWithAuthRequest> requests
    ){
        List<Mono<TaxReceiptValidationResponse>> requestMonos = requests.stream()
                .map(request -> getOAuth2TokenMono()
                        .flatMap(token -> {
                            Map<String, Object> requestBody = objectMapper.convertValue(request, Map.class);
                            log.info("[multipleValidationWithAuth] 추가 인증 데이터를 포함한 세금계산서 다중 검증 시작");

                            return webClientMonoUtil.post(
                                            oAuth2TokenProvider.getTaxReceiptUrl(),
                                            createAuthHeaders(token),
                                            requestBody
                                    )
                                    .flatMap(response -> {
                                        Map<String, Object> responseMap =
                                                taxReceiptWebClientUtil.decodeResponse(response, "multipleValidationWithAuth");
                                        return Mono.just(objectMapper.convertValue(responseMap.get("data"), TaxReceiptValidationResponse.class));
                                    });
                        })
                )
                .collect(Collectors.toList());

        return Flux.merge(requestMonos).collectList();
    }

    private Map<String, String> createAuthHeaders(String token){
        return Map.of(
                "Authorization", "Bearer " + token,
                "Content-Type", MediaType.APPLICATION_JSON_VALUE
        );
    }
}
