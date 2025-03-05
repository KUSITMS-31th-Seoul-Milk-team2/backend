package com.seoulmilk.emp.presentation.swagger;

import com.seoulmilk.core.configuration.swagger.ApiErrorCode;
import com.seoulmilk.core.exception.error.GlobalErrorCode;
import com.seoulmilk.core.infrastructure.security.CustomUserDetails;
import com.seoulmilk.core.presentation.RestResponse;
import com.seoulmilk.emp.dto.reqeust.UpdatePasswordRequest;
import com.seoulmilk.emp.exception.EmpErrorCode;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.RequestBody;

@Tag(name = "MyPage", description = "마이페이지")
public interface UpdatePasswordSwagger {
    @Operation(
            summary = "비밀번호 변경 API",
            description = "비밀번호를 변경합니다.",
            operationId = "/mypage/update-password"
    )
    @ApiErrorCode({GlobalErrorCode.class, EmpErrorCode.class})
    ResponseEntity<RestResponse<String>> updatePassword(
            @Parameter(hidden = true)
            @AuthenticationPrincipal CustomUserDetails customUserDetails,
            @Valid @RequestBody UpdatePasswordRequest updatePasswordRequest
    );
}
