package com.seoulmilk.receipt.dto.request;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;

public record OcrValidationRequest(
        @Schema(description = "사용자 PK", example = "1")
        @NotNull(message = "사용자 PK는 필수입니다.")
        Long empPk,

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

        @Schema(description =  "공급자 사업체명", example = "서울우유협동조합 보문고객센타")
        String supplierName,

        @Schema(description = "공급받는자 사업체명", example = "로쏘(주)")
        String contractorName,

        @Schema(description = "세액", example = "100")
        String taxTotal,

        @Schema(description = "총액(공급가액 + 세액)", example = "100100")
        String grandTotal
) {
}
