package com.seoulmilk.receipt.application;

import com.seoulmilk.receipt.dto.request.OcrValidationRequest;
import com.seoulmilk.receipt.presentation.dto.request.TaxReceiptValidationRequest;
import com.seoulmilk.receipt.presentation.dto.response.AdditionalAuthResponse;
import com.seoulmilk.receipt.presentation.dto.response.TaxReceiptValidationResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
@Log4j2
public class TaxReceiptValidationService {
    private final TaxReceiptValidationProvider taxReceiptValidationProvider;

    @KafkaListener(topics = "${kafka.topic}", groupId = "${kafka.group-id}")
    public void listen(OcrValidationRequest ocrValidationRequest) {
        log.info("[TaxReceiptValidationService] ocr 결과 받은 데이터 - {}", ocrValidationRequest.toString());
    }

    public AdditionalAuthResponse requestAdditionalAuthentication(List<TaxReceiptValidationRequest> requests) {
        return taxReceiptValidationProvider.requestAdditionalAuthentication(requests);
    }

    public List<TaxReceiptValidationResponse> retrieveValidatedTaxReceipts  (String transactionId) {
        return taxReceiptValidationProvider.retrieveValidatedTaxReceipts(transactionId);
    }
}