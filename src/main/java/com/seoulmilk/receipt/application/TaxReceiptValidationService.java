package com.seoulmilk.receipt.application;

import com.seoulmilk.receipt.presentation.dto.request.TaxReceiptValidationRequest;
import com.seoulmilk.receipt.presentation.dto.response.AdditionalAuthResponse;
import com.seoulmilk.receipt.presentation.dto.response.TaxReceiptValidationResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class TaxReceiptValidationService {
    private final TaxReceiptValidationProvider taxReceiptValidationProvider;

    public AdditionalAuthResponse requestAdditionalAuthentication(List<TaxReceiptValidationRequest> requests) {
        return taxReceiptValidationProvider.requestAdditionalAuthentication(requests);
    }

    public List<TaxReceiptValidationResponse> retrieveValidatedTaxReceipts  (String transactionId) {
        return taxReceiptValidationProvider.retrieveValidatedTaxReceipts(transactionId);
    }
}