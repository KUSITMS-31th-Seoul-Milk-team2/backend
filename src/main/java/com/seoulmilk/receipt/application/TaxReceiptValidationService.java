package com.seoulmilk.receipt.application;

import com.seoulmilk.receipt.presentation.dto.request.TaxReceiptValidationRequest;
import io.codef.api.dto.EasyCodefResponse;

import java.util.List;

public interface TaxReceiptValidationService {
    EasyCodefResponse getAdditionalAuthResponse(List<TaxReceiptValidationRequest> requests);
    List<EasyCodefResponse> getMultipleTaxReceiptValidationResponse(String transactionId);
}
