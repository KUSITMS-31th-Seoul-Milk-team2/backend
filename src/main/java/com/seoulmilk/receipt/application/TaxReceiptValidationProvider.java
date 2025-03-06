package com.seoulmilk.receipt.application;

import com.seoulmilk.receipt.dto.request.TaxReceiptValidationRequest;
import com.seoulmilk.receipt.presentation.dto.response.AdditionalAuthResponse;
import com.seoulmilk.receipt.presentation.dto.response.TaxReceiptValidationResponse;

import java.util.List;

public interface TaxReceiptValidationProvider {
    AdditionalAuthResponse requestAdditionalAuthentication(List<TaxReceiptValidationRequest> requests);
    List<TaxReceiptValidationResponse> retrieveValidatedTaxReceipts  (String transactionId);
}
