package com.seoulmilk.receipt.application;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.seoulmilk.receipt.infrastructure.OAuth2TokenProvider;
import com.seoulmilk.receipt.presentation.dto.request.TaxReceiptValidationRequest;
import com.seoulmilk.receipt.presentation.dto.request.TaxReceiptValidationWithAuthRequest;
import com.seoulmilk.receipt.presentation.dto.response.AdditionalAuthResponse;
import com.seoulmilk.receipt.presentation.dto.response.OAuth2TokenResponse;
import com.seoulmilk.receipt.presentation.dto.response.TaxReceiptValidationResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;

import java.net.URLDecoder;
import java.nio.charset.StandardCharsets;
import java.util.Base64;
import java.util.HashMap;
import java.util.Map;

@Service
@RequiredArgsConstructor
@Log4j2
public class TaxReceiptValidationService {
    private final OAuth2TokenProvider oAuth2TokenProvider;

    private ObjectMapper objectMapper = new ObjectMapper();
    private String accessToken;
    /**
     * CODEF OPEN API를 사용하기 위한 OAuth2 토큰을 발급 받습니다
     * @return OAuth2TokenResponse
     */
    private OAuth2TokenResponse getOAuth2Token(){
        String auth = oAuth2TokenProvider.getClientId() + ":" + oAuth2TokenProvider.getClientSecret();
        String authEnc = Base64.getEncoder().encodeToString(auth.getBytes());
        String authHeader = "Basic " + authEnc;

        log.info("[getOAuth2Token] CODEF OPEN API OAuth2Token 발급 시작");

        // OAuth2TokenResponse 반환
        // 아직 redis 도입 이전이므로 추후 refresh token 저장 방안 고민
        String response =  WebClient.create()
                .post()
                .uri(oAuth2TokenProvider.getOAuth2Url())
                .header("Content-Type", "application/x-www-form-urlencoded")
                .header("Authorization", authHeader)
                .bodyValue("grant_type=client_credentials&scope=read")
                .retrieve()
                .bodyToMono(String.class)
                .block();

        try {
            log.info("[getOAuth2Token] CODEF OPEN API OAuth2Token 발급 완료");
            return objectMapper.readValue(response, OAuth2TokenResponse.class);
        } catch (JsonProcessingException e) {
            log.error("[getOAuth2Token] CODEF OPEN API OAuth2Token 발급 실패 - 사유 : {}", e.getMessage());
            throw new RuntimeException(e);
        }
    }

    /**
     * 제3자 세금계산서 발급 사실 조회 최초 요청시 사용합니다.
     * @param taxReceiptValidationRequest
     * @return 추가인증 요청값
     */
    public AdditionalAuthResponse validateTaxReceipt(TaxReceiptValidationRequest taxReceiptValidationRequest){
        OAuth2TokenResponse oAuth2TokenResponse = getOAuth2Token();
        // 추후 redis 적용시 캐시 참고 예정
        accessToken = oAuth2TokenResponse.accessToken();

        // 객체 -> Map 변환
        Map<String, Object> map = objectMapper.convertValue(taxReceiptValidationRequest, Map.class);

        log.info("[validateTaxReceipt] 세금계산서 진위 여부 검증을 위한 데이터 전송 시작");
        String response = WebClient.builder()
                .baseUrl(oAuth2TokenProvider.getTaxReceiptUrl())
                .defaultHeader("Authorization", "Bearer " + accessToken)
                .defaultHeader("Content-Type", MediaType.APPLICATION_JSON_VALUE)
                .build()
                .method(HttpMethod.POST)
                .bodyValue(map)
                .retrieve()
                .bodyToMono(String.class)
                .block();

        Map<String, Object> responseMap = getDataFromDecodedResponse(response, "validateTaxReceipt");

        log.info("[validateTaxReceipt] - 추가 인증 데이터 전송 완료");

        return objectMapper.convertValue(responseMap.get("data"), AdditionalAuthResponse.class);
    }

    /**
     * 추가인증 필요시 사용하는 API입니다.
     * @param taxReceiptValidationWithAuthRequest
     * @return 세금 계산서 검증 사실 여부
     */
    public TaxReceiptValidationResponse validationWithAdditionalAuth(
            TaxReceiptValidationWithAuthRequest taxReceiptValidationWithAuthRequest
    ){
        Map map = objectMapper.convertValue(taxReceiptValidationWithAuthRequest, Map.class);

        log.info("[validationWithAdditionalAuth] 추가 인증 데이터를 포함한 세금계산서 검증 시작");
        String response = WebClient.builder()
                .baseUrl(oAuth2TokenProvider.getTaxReceiptUrl())
                .defaultHeader("Authorization", "Bearer " + accessToken)
                .defaultHeader("Content-Type", MediaType.APPLICATION_JSON_VALUE)
                .build()
                .method(HttpMethod.POST)
                .bodyValue(map)
                .retrieve()
                .bodyToMono(String.class)
                .block();

        Map<String, Object> responseMap = getDataFromDecodedResponse(response, "validationWithAdditionalAuth");

        log.info("[validationWithAdditionalAuth] 추가 인증 데이터를 포함한 세금계산서 검증 완료");
        return objectMapper.convertValue(responseMap.get("data"), TaxReceiptValidationResponse.class);
    }

    /**
     * OPEN API 결과 값에서 data 필드만 빼낸다.
     * @param response
     * @param methodName
     * @return responseMap - data
     */
    private Map<String, Object> getDataFromDecodedResponse(String response, String methodName){
        // response data는 디코딩이 필요함
        String decodedResponse = URLDecoder.decode(response, StandardCharsets.UTF_8);
        log.info("[{}] 받은 데이터 - {}", methodName, decodedResponse);

        // 결과값에서 data 필드만 빼온다.
        Map<String, Object> responseMap = null;
        try{
            responseMap = objectMapper.readValue(decodedResponse, Map.class);
        }catch(Exception e){
            log.info("[{}}] - 추가 인증 데이터 전송 실패 - 사유 : {}",methodName, e.getMessage());
            throw new RuntimeException(e);
        }

        return responseMap;
    }
}
