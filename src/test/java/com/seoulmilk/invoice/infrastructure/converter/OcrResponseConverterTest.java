package com.seoulmilk.invoice.infrastructure.converter;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

class OcrResponseConverterTest {

    @Test
    void 사업자등록번호_유효성_검증_테스트() {
        String validNumber = "123-45-67890";
        String cleaned = InvoiceRegex.NON_DIGIT.removeFrom(validNumber); // 1234567890
        assertTrue(
                InvoiceRegex.BUSINESS_REGISTER_NUMBER.getCompiledPattern()
                        .matcher(cleaned).matches()
        );
    }

    @Test
    void 승인번호_포맷팅_테스트() {
        String raw = "2025-03-11-ABCDE";
        String result = InvoiceRegex.HYPHEN.removeFrom(raw)
                .replaceAll(InvoiceRegex.WHITESPACE.getPattern(), "");
        assertEquals("20250311ABCDE", result);

    }
}
