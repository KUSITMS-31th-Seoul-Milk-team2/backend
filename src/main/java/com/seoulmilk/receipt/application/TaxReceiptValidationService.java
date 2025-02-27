package com.seoulmilk.receipt.application;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.seoulmilk.receipt.exception.ReceiptErrorCode;
import com.seoulmilk.receipt.infrastructure.OAuth2TokenProvider;
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

import java.util.Base64;
import java.util.Map;

@Service
@RequiredArgsConstructor
@Log4j2
public class TaxReceiptValidationService {
    private final OAuth2TokenProvider oAuth2TokenProvider;
    private final TaxReceiptWebClientUtil taxReceiptWebClientUtil;

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

        Map<String, String> headers = Map.of(
                "Content-Type", "application/x-www-form-urlencoded",
                "Authorization", authHeader
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
        Map<String, Object> requestBody = objectMapper.convertValue(taxReceiptValidationRequest, Map.class);

        log.info("[validateTaxReceipt] 세금계산서 진위 여부 검증을 위한 데이터 전송 시작");
        String response = taxReceiptWebClientUtil.post(
                oAuth2TokenProvider.getTaxReceiptUrl(),
                createAuthHeaders(),
                requestBody
        );

        Map<String, Object> responseMap = taxReceiptWebClientUtil.decodeResponse(
                response,
                "validateTaxReceipt"
        );

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
        Map requestBody = objectMapper.convertValue(taxReceiptValidationWithAuthRequest, Map.class);

        log.info("[validationWithAdditionalAuth] 추가 인증 데이터를 포함한 세금계산서 검증 시작");
        String response = taxReceiptWebClientUtil.post(
                oAuth2TokenProvider.getTaxReceiptUrl(),
                createAuthHeaders(),
                requestBody
        );

        Map<String, Object> responseMap = taxReceiptWebClientUtil.decodeResponse(
                response,
                "validationWithAdditionalAuth"
        );

        log.info("[validationWithAdditionalAuth] 추가 인증 데이터를 포함한 세금계산서 검증 완료");
        return objectMapper.convertValue(responseMap.get("data"), TaxReceiptValidationResponse.class);
    }

    private Map<String, String> createAuthHeaders(){
        return Map.of(
                "Authorization", "Bearer " + accessToken,
                "Content-Type", MediaType.APPLICATION_JSON_VALUE
        );
    }
}
