package com.seoulmilk.auth.presentation.swagger;

import com.seoulmilk.auth.exception.AuthenticationErrorCode;
import com.seoulmilk.auth.presentation.dto.request.LoginRequest;
import com.seoulmilk.auth.presentation.dto.response.LoginResponse;
import com.seoulmilk.core.configuration.swagger.ApiErrorCode;
import com.seoulmilk.core.exception.error.GlobalErrorCode;
import com.seoulmilk.core.presentation.RestResponse;
import com.seoulmilk.emp.exception.EmpErrorCode;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.ResponseEntity;

@Tag(name = "Auth", description = "인증/인가")
public interface LoginSwagger {

    @Operation(
            summary = "로그인 API",
            description = "사용자가 사번과 비밀번호를 입력하여 로그인을 진행합니다.",
            operationId = "/v1/auth/login"
    )
    @ApiErrorCode({GlobalErrorCode.class, AuthenticationErrorCode.class, EmpErrorCode.class})
    ResponseEntity<RestResponse<LoginResponse>> login(LoginRequest request);
}
