package com.seoulmilk.receipt.application;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.seoulmilk.receipt.infrastructure.OAuth2TokenProvider;
import com.seoulmilk.receipt.infrastructure.webclient.TaxReceiptWebClientUtil;
import com.seoulmilk.receipt.presentation.dto.request.TaxReceiptValidationRequest;
import com.seoulmilk.receipt.presentation.dto.request.TaxReceiptValidationWithAuthRequest;
import com.seoulmilk.receipt.presentation.dto.response.AdditionalAuthResponse;
import com.seoulmilk.receipt.presentation.dto.response.TaxReceiptValidationResponse;
import io.codef.api.EasyCodef;
import io.codef.api.EasyCodefServiceType;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

@Service
@RequiredArgsConstructor
@Log4j2
public class ValidationWithEasyCodefService {
    private final OAuth2TokenProvider oAuth2TokenProvider;
    private final TaxReceiptWebClientUtil taxReceiptWebClientUtil;
    private final ObjectMapper objectMapper;

    // codef 서비스 설정
    private static final EasyCodefServiceType serviceType = EasyCodefServiceType.DEMO;
    private final String endPoint = "/v1/kr/public/nt/third-party/tax-invoice-issue";

    public List<AdditionalAuthResponse> getAdditionalAuthResponses(List<TaxReceiptValidationRequest> requests) throws InterruptedException {
        EasyCodef easyCodef = new EasyCodef();
        easyCodef.setPublicKey(oAuth2TokenProvider.getPublicKey());
        easyCodef.setClientInfoForDemo(oAuth2TokenProvider.getClientId(), oAuth2TokenProvider.getClientSecret());

        List<AdditionalAuthResponse> responses = new CopyOnWriteArrayList<>();
        ScheduledExecutorService scheduler = Executors.newScheduledThreadPool(1); // 단일 스레드 사용

        for (int i = 0; i < requests.size(); i++) {
            final int index = i;
            scheduler.schedule(() -> {
                try {
                    TaxReceiptValidationRequest request = requests.get(index);
                    HashMap<String, Object> requestBody = objectMapper.convertValue(request, HashMap.class);

                    log.info("[getAdditionalAuthResponses] 보낸 데이터 - {}", requestBody);
                    String response = easyCodef.requestProduct(endPoint, serviceType, requestBody);
                    log.info("[getAdditionalAuthResponses] 받은 데이터 - {}", response);

                    Map<String, Object> responseMap = taxReceiptWebClientUtil.decodeResponse(response, "getAdditionalAuthResponses");
                    responses.add(objectMapper.convertValue(responseMap.get("data"), AdditionalAuthResponse.class));
                } catch (Exception e) {
                    log.error("Error processing request", e);
                }
            }, i * 500, TimeUnit.MILLISECONDS); // 요청을 0.5초 간격으로 실행
        }

        scheduler.shutdown();
        scheduler.awaitTermination(10, TimeUnit.SECONDS); // 최대 10초 기다린 후 종료

        return new ArrayList<>(responses);
    }

    public List<TaxReceiptValidationResponse> getValidationResponses(
            List<TaxReceiptValidationWithAuthRequest> requests
    ) throws InterruptedException {
        EasyCodef easyCodef = new EasyCodef();
        easyCodef.setPublicKey(oAuth2TokenProvider.getPublicKey());
        easyCodef.setClientInfoForDemo(oAuth2TokenProvider.getClientId(), oAuth2TokenProvider.getClientSecret());

        List<TaxReceiptValidationResponse> responses = new CopyOnWriteArrayList<>();
        Map<String, TaxReceiptValidationResponse> transactionCache = new HashMap<>(); // 성공한 응답 저장
        ScheduledExecutorService scheduler = Executors.newScheduledThreadPool(1);

        for(int i = 0; i < requests.size(); i++) {
            final int index = i;
            scheduler.schedule(() -> {
                try {
                    TaxReceiptValidationWithAuthRequest request = requests.get(index);
                    HashMap<String, Object> requestBody = objectMapper.convertValue(request, HashMap.class);

                    log.info("[getAdditionalAuthResponses] 보낸 데이터 - {}", requestBody);
                    String response = easyCodef.requestCertification(endPoint, serviceType, requestBody);
                    log.info("[getAdditionalAuthResponses] 받은 데이터 - {}", response);

                    Map<String, Object> responseMap = taxReceiptWebClientUtil.decodeResponse(response, "getAdditionalAuthResponses");
                    Map<String, Object> result = (Map<String, Object>) responseMap.get("result");
                    Map<String, Object> data = (Map<String, Object>) responseMap.get("data");

                    String transactionId = (String) result.get("transactionId");
                    String code = (String) result.get("code");

                    if ("CF-00000".equals(code)) { // 정상 응답이면 저장
                        TaxReceiptValidationResponse taxReceiptValidationResponse =
                                objectMapper.convertValue(data, TaxReceiptValidationResponse.class);
                        responses.add(taxReceiptValidationResponse);
                        transactionCache.put(transactionId, taxReceiptValidationResponse);
                    } else if ("CF-00025".equals(code)) { // 이미 응답이 완료된 요청이라면?
                        if (transactionCache.containsKey(transactionId)) {
                            responses.add(transactionCache.get(transactionId)); // 기존 응답을 재사용
                        } else {
                            log.warn("기존 응답을 찾을 수 없습니다. Transaction ID: {}", transactionId);
                        }
                    }
                } catch (Exception e) {
                    log.error("Error processing request", e);
                }
            }, i * 500, TimeUnit.MILLISECONDS); // 요청을 0.5초 간격으로 실행
        }

        scheduler.shutdown();
        scheduler.awaitTermination(10, TimeUnit.SECONDS); // 최대 10초 기다린 후 종료

        return responses;
    }
}