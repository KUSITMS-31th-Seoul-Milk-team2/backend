package com.seoulmilk.receipt.presentation.dto.response;

import io.swagger.v3.oas.annotations.media.Schema;

public record AdditionalAuthRequest(
        @Schema(description = "추가 인증 필요 유무", example = "true")
        Boolean continue2Way,

        @Schema(description = "추가 인증 방식", example = "추가 인증 정보")
        String method,

        @Schema(description = "잡 인덱스", example = "추가 인증 정보, 추가 요청에 사용")
        Integer jobIndex,

        @Schema(description = "스레드 인덱스", example = "추가 인증 정보, 추가 요청에 사용")
        Integer threadIndex,

        @Schema(description = "트랜잭션 아이디", example = "추가 인증 정보, 추가 요청에 사용")
        String jti,

        @Schema(description = "추가 인증 시간", example = "추가 인증 정보, 추가 요청에 사용")
        String twoWayTimestamp
) {
}
