package com.seoulmilk.receipt.presentation.dto.response;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;

public record TaxReceiptDataResponse(
        @NotNull(message = "공급자 등록 번호는 필수 입력 항목입니다.")
        @Schema(description = "공급자 등록 번호", example = "10자리 숫자 (1234567890)")
        String supplierRegNumber,

        @NotNull(message = "공급 받는자 등록번호는 필수 입력 항목입니다.")
        @Schema(description = "공급받는자 등록번호", example = "10자리 숫자 (1234567890)")
        String contractorRegNumber,

        @NotNull(message = "승인번호는 필수 입력 항목입니다.")
        @Schema(description = "승인번호", example = "숫자만 입력하셔야 합니다")
        String approvalNo,

        @NotNull(message = "작성일자는 필수 입력 항목입니다.")
        @Schema(description = "작성일자", example = "YYYYMMDD")
        String reportingDate,

        @NotNull(message = "공급가액은 필수 입력 항목입니다.")
        @Schema(description = "공급가액", example = "4560000")
        String supplyValue,

        @NotNull(message = "파일 URL은 필수 입력 항목입니다.")
        @Schema(description = "파일 URL", example = "file.url")
        String fileUrl

) {
}
