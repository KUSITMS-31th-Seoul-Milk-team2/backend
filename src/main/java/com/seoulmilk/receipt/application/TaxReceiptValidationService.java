package com.seoulmilk.receipt.application;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.seoulmilk.receipt.exception.ReceiptErrorCode;
import com.seoulmilk.receipt.infrastructure.configuration.EasyCodefProvider;
import com.seoulmilk.receipt.infrastructure.factory.EasyCodefRequestFactory;
import com.seoulmilk.receipt.presentation.dto.request.TaxReceiptValidationRequest;
import com.seoulmilk.receipt.presentation.dto.response.AdditionalAuthResponse;
import com.seoulmilk.receipt.presentation.dto.response.TaxReceiptValidationResponse;
import io.codef.api.EasyCodef;
import io.codef.api.dto.EasyCodefRequest;
import io.codef.api.dto.EasyCodefResponse;
import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;

@Service
@RequiredArgsConstructor
@Log4j2
public class TaxReceiptValidationService {
    private final EasyCodefProvider easyCodefProvider;
    private final EasyCodefRequestFactory easyCodefRequestFactory;
    private final ObjectMapper objectMapper;

    private EasyCodef easyCodef;

    @PostConstruct
    private void setEasyCodef(){
        easyCodef = easyCodefProvider.getEasyCodef();
    }

    public AdditionalAuthResponse requestAdditionalAuthentication(List<TaxReceiptValidationRequest> requests) {
        List<EasyCodefRequest> easyCodefRequests = new LinkedList<>();

        for(TaxReceiptValidationRequest request : requests) {
            easyCodefRequests.add(easyCodefRequestFactory.createTaxReceiptRequest(request));
        }

        EasyCodefResponse response = null;
        try{
            response = easyCodef.requestMultipleProduct(easyCodefRequests);
        } catch (Exception e){
            throw ReceiptErrorCode.ERROR_TO_CONNECT_CODEF_SERVER.toException();
        }

        // 응답 성공시 추가인증 관련 정보를 받는다.
        if(response.code().equals("CF-03002")){
            HashMap<String, Object> responseMap = objectMapper.convertValue(response, HashMap.class);
            return objectMapper.convertValue(responseMap.get("data"), AdditionalAuthResponse.class);
        }else{
            throw ReceiptErrorCode.ERROR_TO_GET_DATA.toException();
        }
    }

    public List<TaxReceiptValidationResponse> retrieveValidatedTaxReceipts  (String transactionId) {
        List<EasyCodefResponse> easyCodefResponses;
        try{
            easyCodefResponses = easyCodef.requestMultipleSimpleAuthCertification(transactionId);
        }catch (Exception e){
            throw ReceiptErrorCode.ADDITIONAL_AUTHENTICATION_ERROR.toException();
        }

        List<TaxReceiptValidationResponse> validationResponses = new LinkedList<>();

        for(EasyCodefResponse easyCodefResponse : easyCodefResponses){
            if(easyCodefResponse.code().equals("CF-00000")){
                HashMap<String, Object> responseMap = objectMapper.convertValue(easyCodefResponse, HashMap.class);
                validationResponses.add(objectMapper.convertValue(responseMap.get("data"), TaxReceiptValidationResponse.class));
            }else{
                // 오류 처리 방안 고민....
                throw ReceiptErrorCode.INVALID_FORMAT_ERROR.toException();
            }
        }

        return validationResponses;
    }
}