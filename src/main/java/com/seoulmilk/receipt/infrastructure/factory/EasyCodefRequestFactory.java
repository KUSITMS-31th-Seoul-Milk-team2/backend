package com.seoulmilk.receipt.infrastructure.factory;

import com.seoulmilk.receipt.infrastructure.properties.EasyCodefProperties;
import com.seoulmilk.receipt.presentation.dto.request.TaxReceiptValidationRequest;
import io.codef.api.dto.EasyCodefRequest;
import io.codef.api.dto.EasyCodefRequestBuilder;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class EasyCodefRequestFactory {
    private final EasyCodefProperties properties;

    public EasyCodefRequest createTaxReceiptRequest(TaxReceiptValidationRequest request){
        return EasyCodefRequestBuilder.builder()
                .path(properties.getEndpoint())
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
