package com.seoulmilk.receipt.dto.request;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;

public record OcrValidationRequest(
        @Schema(description = "사용자 PK", example = "1")
        @NotNull(message = "사용자 PK는 필수입니다.")
        Long empPk,

        @Schema(description = "파일 URL", example = "http://localhost:8080/ocr/1")
        @NotNull(message = "파일 URL은 필수입니다.")
        String fileUrl,

        @Schema(description = "국세청 검증을 위한 정보")
        TaxValidationInfo taxValidationInfo

) {
    public record TaxValidationInfo(
            @Schema(description = "공급자 등록번호", example = "1234567890")
            @Pattern(regexp = "^[0-9]{10}$")
            String supplierRegNumber,

            @Schema(description = "공급받는자 등록번호", example = "1234567890")
            @Pattern(regexp = "^[0-9]{10}$")
            String contractorRegNumber,

            @Schema(description = "승인번호", example = "1234567890")
            String approvalNo,

            @Schema(description = "작성일자", example = "20210101")
            String reportingDate,

            @Schema(description = "공급가액", example = "100000")
            String supplyValue,

            @Schema(description = "공급자 사업체명", example = "서울우유협동조합 보문고객센타")
            String supplierName,

            @Schema(description = "공급받는자 사업체명", example = "로쏘(주)")
            String contractorName,

            @Schema(description = "세액", example = "100")
            String taxTotal,

            @Schema(description = "총액(공급가액 + 세액)", example = "100100")
            String grandTotal
    ) {

        private static final String DEFAULT_SUPPLIER_REG_NUMBER = "1234567890";
        private static final String DEFAULT_CONTRACTOR_REG_NUMBER = "1234567890";
        private static final String DEFAULT_APPROVAL_NO = "123456781234567812345678";
        private static final String DEFAULT_REPORTING_DATE = "20250305";
        private static final String DEFAULT_SUPPLY_VALUE = "0";
        private static final String DEFAULT_SUPPLIER_NAME = "이름";
        private static final String DEFAULT_CONTRACTOR_NAME = "이름";
        private static final String DEFAULT_TAX_TOTAL = "0";
        private static final String DEFAULT_GRAND_TOTAL = "100";


        public static TaxValidationInfo from(
                String supplierRegNumber,
                String contractorRegNumber,
                String approvalNo,
                String reportingDate,
                String supplyValue,
                String supplierName,
                String contractorName,
                String taxTotal,
                String grandTotal
        ) {
            return new TaxValidationInfo(
                    validateOrDefault(supplierRegNumber, DEFAULT_SUPPLIER_REG_NUMBER, "^[0-9]{10}$"),
                    validateOrDefault(contractorRegNumber, DEFAULT_CONTRACTOR_REG_NUMBER, "^[0-9]{10}$"),
                    validateOrDefault(approvalNo, DEFAULT_APPROVAL_NO, "^[0-9]{24}$"),
                    validateOrDefault(reportingDate, DEFAULT_REPORTING_DATE, "^[0-9]{8}$"),
                    validateOrDefault(supplyValue, DEFAULT_SUPPLY_VALUE, "^[0-9]+$"),
                    defaultIfNull(supplierName, DEFAULT_SUPPLIER_NAME),
                    defaultIfNull(contractorName, DEFAULT_CONTRACTOR_NAME),
                    validateOrDefault(taxTotal, DEFAULT_TAX_TOTAL, "^[0-9]+$"),
                    validateOrDefault(grandTotal, DEFAULT_GRAND_TOTAL, "^[0-9]+$")
            );
        }

        private static String validateOrDefault(String value, String defaultValue, String regex) {
            if (value == null || !value.matches(regex)) {
                return defaultValue;
            }
            return value;
        }

        private static String defaultIfNull(String value, String defaultValue) {
            return value != null ? value : defaultValue;
        }
    }
}
