package com.seoulmilk.emp.dto.response;

import io.swagger.v3.oas.annotations.media.Schema;

public record UpdateHometaxInfoResponse(
        @Schema(name = "성공 여부", example = "true")
        Boolean isSuccess,

        @Schema(name = "메시지", example = "홈택스 정보가 성공적으로 업데이트 되었습니다.")
        String message
) {
    public static UpdateHometaxInfoResponse of(Boolean isSuccess, String message) {
        return new UpdateHometaxInfoResponse(isSuccess, message);
    }
}
