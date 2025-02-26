package com.seoulmilk.invoice.infrastructure.converter;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.seoulmilk.invoice.dto.request.OcrRequest;
import com.seoulmilk.invoice.exception.InvoiceErrorCode;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@RequiredArgsConstructor
@Component
public class OcrRequestConverter {

    private final ObjectMapper objectMapper;

    public String toJson(OcrRequest request) {
        try {
            return objectMapper.writeValueAsString(request);
        } catch (Exception e) {
            throw InvoiceErrorCode.FAILED_TO_CREATE_JSON.toException();
        }
    }
}
