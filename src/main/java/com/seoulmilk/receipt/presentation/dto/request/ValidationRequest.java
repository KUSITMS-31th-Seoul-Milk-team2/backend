package com.seoulmilk.receipt.presentation.dto.request;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;

public record ValidationRequest(
        @NotNull(message = "공급자 등록 번호는 필수 입력 항목입니다.")
        @Schema(description = "공급자 등록 번호", example = "3062870320")
        String supplierRegNumber,

        @NotNull(message = "공급 받는자 등록번호는 필수 입력 항목입니다.")
        @Schema(description = "공급받는자 등록번호", example = "3088509085")
        String contractorRegNumber,

        @NotNull(message = "승인번호는 필수 입력 항목입니다.")
        @Schema(description = "승인번호", example = "202406304100000578475123")
        String approvalNo,

        @NotNull(message = "작성일자는 필수 입력 항목입니다.")
        @Schema(description = "작성일자", example = "20240630")
        String reportingDate,

        @NotNull(message = "공급가액은 필수 입력 항목입니다.")
        @Schema(description = "공급가액", example = "23930493")
        String supplyValue
) {
}
