package com.seoulmilk.emp.presentation.swagger;

import com.seoulmilk.core.configuration.swagger.ApiErrorCode;
import com.seoulmilk.core.infrastructure.security.CustomUserDetails;
import com.seoulmilk.core.presentation.RestResponse;
import com.seoulmilk.emp.dto.request.CheckPasswordCorrectRequest;
import com.seoulmilk.emp.dto.response.CheckPasswordCorrectResponse;
import com.seoulmilk.emp.exception.EmpErrorCode;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.RequestBody;

@Tag(name = "MyPage", description = "마이페이지")
public interface CheckPasswordCorrectSwagger {
    @Operation(
            summary = "비밀번호 일치 확인 API",
            description = "비밀번호가 일치하는지 확인합니다.",
            operationId = "/v1/emp/check-password-correct"
    )
    @ApiErrorCode({EmpErrorCode.class})
    ResponseEntity<RestResponse<CheckPasswordCorrectResponse>> checkPasswordCorrect(
            @Parameter(hidden = true)
            @AuthenticationPrincipal CustomUserDetails customUserDetails,
            @RequestBody CheckPasswordCorrectRequest checkPasswordCorrectRequest
        );
}
