package com.seoulmilk.receipt.dto.request;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;

public record UpdateValidReceiptRequest(
        @NotNull(message = "계산서 ID는 필수입니다.")
        @Schema(description = "계산서 ID", example = "1")
        Long id,

        @Schema(description = "총 공급가액 합계", example = "200000")
        Integer chargeTotal,

        @Schema(description = "총 세액 합계", example = "56100")
        Integer taxTotal,

        @Schema(description = "총액", example = "256100")
        Integer grandTotal,

        @Schema(description = "생성일", example = "2025-03-10")
        String erdat,

        @Schema(description = "생성 시간", example = "12:15:10")
        String erzet
) {
}
