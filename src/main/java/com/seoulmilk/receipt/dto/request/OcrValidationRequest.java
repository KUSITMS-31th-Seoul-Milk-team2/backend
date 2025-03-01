package com.seoulmilk.receipt.dto.request;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Pattern;

public record OcrValidationRequest(
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
        String supplyValue
) {
}
