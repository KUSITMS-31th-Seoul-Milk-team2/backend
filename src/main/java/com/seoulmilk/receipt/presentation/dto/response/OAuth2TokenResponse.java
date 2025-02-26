package com.seoulmilk.receipt.presentation.dto.response;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

public record OAuth2TokenResponse(
        @JsonProperty("access_token") String accessToken,
        @JsonProperty("token_type") String tokenType,
        @JsonProperty("expires_in") Long expiresIn,
        @JsonProperty("scope") String scope
) {
    @JsonCreator
    public OAuth2TokenResponse {
        // 아무것도 안 해도 됨. 단순히 Jackson이 인식하게 하는 역할
    }
}
