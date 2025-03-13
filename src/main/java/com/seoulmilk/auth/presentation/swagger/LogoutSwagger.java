package com.seoulmilk.auth.presentation.swagger;

import com.seoulmilk.core.configuration.swagger.ApiErrorCode;
import com.seoulmilk.core.presentation.RestResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.ResponseEntity;

@Tag(name = "Auth", description = "인증/인가")
public interface LogoutSwagger {

    @Operation(
            summary = "로그아웃 API",
            description = "사용자가 로그아웃을 진행합니다.",
            operationId = "/v1/auth/logout"
    )
    @ApiErrorCode({})
    ResponseEntity<RestResponse<Void>> logout();
}
