package com.seoulmilk.emp.dto.response;

import io.swagger.v3.oas.annotations.media.Schema;

public record CheckPasswordCorrectResponse(
        @Schema(description = "메시지", example = "비밀번호가 일치합니다.")
        String message
) {
    public static CheckPasswordCorrectResponse of(String message) {
        return new CheckPasswordCorrectResponse(message);
    }
}
