package com.seoulmilk.receipt.presentation.dto.response;

import io.swagger.v3.oas.annotations.media.Schema;

public record OAuth2TokenResponse(
        @Schema(description = "accessToken", example = "OPENJ API 서버 접근용 accessToken")
        String accessToken,

        @Schema(description = "RefreshToken", example = "OPEN API 서버 전용 refreshToken")
        String refreshToken
) {
}
