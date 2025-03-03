package com.seoulmilk.receipt.application;

import com.seoulmilk.receipt.infrastructure.EasyCodefProvider;
import com.seoulmilk.receipt.infrastructure.webclient.TaxReceiptWebClientUtil;
import com.seoulmilk.receipt.presentation.dto.request.TaxReceiptValidationRequest;
import io.codef.api.EasyCodef;
import io.codef.api.EasyCodefBuilder;
import io.codef.api.constants.CodefClientType;
import io.codef.api.dto.EasyCodefRequest;
import io.codef.api.dto.EasyCodefRequestBuilder;
import io.codef.api.dto.EasyCodefResponse;
import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.stereotype.Service;

import java.util.LinkedList;
import java.util.List;

@Service
@RequiredArgsConstructor
@Log4j2
public class TaxReceiptValidationServiceImpl implements TaxReceiptValidationService {
    private final TaxReceiptWebClientUtil taxReceiptWebClientUtil;
    private final EasyCodefProvider easyCodefProvider;

    private EasyCodef easyCodef;

    @PostConstruct
    private void setEasyCodef(){
        easyCodef = easyCodefProvider.getEasyCodef();
    }

    @Override
    public EasyCodefResponse requestAdditionalAuthentication(List<TaxReceiptValidationRequest> requests) {
        List<EasyCodefRequest> easyCodefRequests = new LinkedList<>();

        for(TaxReceiptValidationRequest request : requests) {
            EasyCodefRequest easyCodefRequest = requestBuilder(request);
            easyCodefRequests.add(easyCodefRequest);
        }

        EasyCodefResponse response = easyCodef.requestMultipleProduct(easyCodefRequests);
        return response;
    }

    @Override
    public List<EasyCodefResponse> requestMultipleTaxReceiptValidation (String transactionId) {
        List<EasyCodefResponse> easyCodefResponses = easyCodef.requestMultipleSimpleAuthCertification(
                transactionId
        );

        return easyCodefResponses;
    }

    private EasyCodefRequest requestBuilder(TaxReceiptValidationRequest request){
        return EasyCodefRequestBuilder.builder()
                .path("/v1/kr/public/nt/third-party/tax-invoice-issue")
                .requestBody("organization", request.organization())
                .requestBody("loginType", request.loginType())
                .requestBody("id", request.id())
                .requestBody("loginTypeLevel", request.loginTypeLevel())
                .requestBody("userName", request.userName())
                .requestBody("phoneNo", request.phoneNo())
                .requestBody("identity", request.identity())
                .requestBody("supplierRegNumber", request.supplierRegNumber())
                .requestBody("contractorRegNumber", request.contractorRegNumber())
                .requestBody("approvalNo", request.approvalNo())
                .requestBody("reportingDate", request.reportingDate())
                .requestBody("supplyValue", request.supplyValue())
                .requestBody("telecom", request.telecom())
                .build();
    }
}