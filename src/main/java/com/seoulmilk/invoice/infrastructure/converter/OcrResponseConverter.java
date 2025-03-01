package com.seoulmilk.invoice.infrastructure.converter;

import com.seoulmilk.invoice.dto.response.OcrResponse;
import com.seoulmilk.invoice.exception.InvoiceErrorCode;
import com.seoulmilk.receipt.dto.request.OcrValidationRequest;
import lombok.extern.slf4j.Slf4j;

import java.util.Map;
import java.util.stream.Collectors;

@Slf4j
public class OcrResponseConverter {
    public static OcrValidationRequest convert(OcrResponse response) {
        Map<String, String> fieldMap = extractFieldMap(response);

        return new OcrValidationRequest(
                normalizeRegNumber(fieldMap.get("공급자 사업자등록번호")),
                normalizeRegNumber(fieldMap.get("공급받는자 사업자등록번호")),
                formatApprovalNo(fieldMap.get("승인번호").replaceAll("\\s+", "")),
                formatDate(fieldMap.get("전자세금계산서 작성일자")),
                normalizeSupplyValue(fieldMap.get("총 공급가액"))
        );
    }

    private static Map<String, String> extractFieldMap(OcrResponse response) {
        return response.images().get(0).fields().stream()
                .collect(
                        Collectors.toMap(
                                field -> field.name(),
                                field -> field.inferText()
                        )
                );
    }

    private static String normalizeRegNumber(String raw) {
        System.out.println("raw: " + raw);
        String cleaned = raw.replaceAll("[^0-9]", "");
        if (!cleaned.matches("^\\d{10}$")) {
            throw InvoiceErrorCode.INVALID_SUPPLIER_NUMBER_FORMAT.toException();
        }
        return cleaned;
    }

    private static String formatApprovalNo(String raw) {
        return raw.replaceAll("-", "").replaceAll("\\s+", "");
    }

    private static String formatDate(String rawDate) {
        return rawDate.replaceAll("\\s+", "");
    }

    private static String normalizeSupplyValue(String raw) {
        return raw.replaceAll("[^0-9]", "");
    }
}
