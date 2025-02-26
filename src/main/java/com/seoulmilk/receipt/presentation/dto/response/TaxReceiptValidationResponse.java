package com.seoulmilk.receipt.presentation.dto.response;

import com.fasterxml.jackson.annotation.JsonProperty;
import io.swagger.v3.oas.annotations.media.Schema;

public record TaxReceiptValidationResponse(
        @JsonProperty
        @Schema(description = "진위확인", example = "(\"0\": false, \"1\": true)")
        String resAuthenticity,

        @JsonProperty
        @Schema(
                description = "진위확인 내용",
                example = "\t(ex. \"조회 요청한 전자세금계산서는 2022년 01월 10일 발급된 사실이 있습니다.”)"
        )
        String resAuthenticityDesc
) {

}
