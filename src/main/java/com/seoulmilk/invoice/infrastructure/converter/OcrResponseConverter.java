package com.seoulmilk.invoice.infrastructure.converter;

import com.seoulmilk.invoice.dto.response.OcrResponse;
import com.seoulmilk.invoice.exception.InvoiceErrorCode;
import com.seoulmilk.receipt.dto.request.OcrValidationRequest;
import lombok.extern.slf4j.Slf4j;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Slf4j
public class OcrResponseConverter {

    public static List<String> REQUIRED_FIELDS = List.of(
        "공급자 사업자등록번호", "공급받는자 사업자등록번호",
        "승인번호", "전자세금계산서 작성일자", "총 공급가액"
    );

    public static OcrValidationRequest convert(OcrResponse response) {
        Map<String, String> fieldMap = extractFieldMap(response);

        REQUIRED_FIELDS.forEach(field -> {
            if (fieldMap.get(field) == null) {
                log.error("국세청 검증을 위한 필수 필드 누락: {}", field);
                throw InvoiceErrorCode.MISSING_REQUIRED_FIELD.toException();
            }
        });

        return new OcrValidationRequest(
                normalizeRegNumber(fieldMap.get("공급자 사업자등록번호")),
                normalizeRegNumber(fieldMap.get("공급받는자 사업자등록번호")),
                formatApprovalNo(fieldMap.get("승인번호").replaceAll("\\s+", "")),
                formatDate(fieldMap.get("전자세금계산서 작성일자")),
                normalizeSupplyValue(fieldMap.get("총 공급가액"))
        );
    }

    private static Map<String, String> extractFieldMap(OcrResponse response) {
        if (response.images() == null || response.images().isEmpty()) {
            throw InvoiceErrorCode.INVALID_OCR_RESPONSE.toException();
        }

        return response.images().get(0).fields().stream()
                .collect(
                        Collectors.toMap(
                                field -> field.name(),
                                field -> field.inferText()
                        )
                );
    }

    private static String normalizeRegNumber(String raw) {
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
