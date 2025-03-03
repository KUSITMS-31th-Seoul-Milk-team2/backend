package com.seoulmilk.receipt.application;

import com.seoulmilk.receipt.infrastructure.EasyCodefFactory;
import com.seoulmilk.receipt.infrastructure.configuration.EasyCodefProvider;
import com.seoulmilk.receipt.presentation.dto.request.TaxReceiptValidationRequest;
import io.codef.api.EasyCodef;
import io.codef.api.dto.EasyCodefRequest;
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
    private final EasyCodefProvider easyCodefProvider;
    private final EasyCodefFactory easyCodefFactory;

    private EasyCodef easyCodef;

    @PostConstruct
    private void setEasyCodef(){
        easyCodef = easyCodefProvider.getEasyCodef();
    }

    @Override
    public EasyCodefResponse requestAdditionalAuthentication(List<TaxReceiptValidationRequest> requests) {
        List<EasyCodefRequest> easyCodefRequests = new LinkedList<>();

        for(TaxReceiptValidationRequest request : requests) {
            easyCodefRequests.add(easyCodefFactory.createTaxReciptReQuest(request));
        }

        EasyCodefResponse response = easyCodef.requestMultipleProduct(easyCodefRequests);
        return response;
    }

    @Override
    public List<EasyCodefResponse> requestMultipleTaxReceiptValidation (String transactionId) {
        List<EasyCodefResponse> easyCodefResponses =
                easyCodef.requestMultipleSimpleAuthCertification(transactionId);

        return easyCodefResponses;
    }
}