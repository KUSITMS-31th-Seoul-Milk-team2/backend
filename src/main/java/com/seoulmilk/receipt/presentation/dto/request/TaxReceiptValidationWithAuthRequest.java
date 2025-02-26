package com.seoulmilk.receipt.presentation.dto.request;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;

public record TaxReceiptValidationWithAuthRequest(
        @Schema(description = " 이전 요청값과 동일", example = "1차 요청시 요청값과 같은 값")
        TaxReceiptValidationRequest baseRequest,

        @NotNull(message = "간편인증 여부는 필수 입력 항목입니다")
        @Schema(description = "간편인증 여부", example = "\"0\": cancel , \"1\": ok")
        String simpleAuth,

        @NotNull(message = "추가 요청 설명값을 입력해주세요(true 고정)")
        @Schema(description = "추가 요청임을 알려주는 설정값", example = "true값 고정")
        Boolean is2Way,

        @Schema(description = "1차 요청 응답 값", example = "Object 타입")
        TwoWayInfo twoWayInfo
) {
    public record TwoWayInfo(
            @NotNull(message = "잡 인덱스는 필수 입력 값입니다.(응답값과 동일)")
            @Schema(description = "잡 인덱스", example = "0")
            Integer jobIndex,

            @NotNull(message = "스레드 인덱스는 필수 입력 값입니다.(응답값과 동일)")
            @Schema(description = "스레드 인덱스", example = "0")
            Integer threadIndex,

            @NotNull(message = "트랜잭션 아이디를 필수로 입력해야 합니다.(응답값과 동일)")
            @Schema(description = "트랜잭션 아이디", example = "db55392ae72a44efaa394")
            String jti,

            @NotNull(message = "추가 인증 시간을 필수로 입력해야 합니다.(응답값과 동일)")
            @Schema(description = "추가 인증 시간", example = "15650663")
            Long twoWayTimestamp
    ) {
    }
}
