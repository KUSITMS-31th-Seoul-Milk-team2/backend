package com.seoulmilk.emp.presentation.swagger;

import com.seoulmilk.core.configuration.swagger.ApiErrorCode;
import com.seoulmilk.core.exception.error.GlobalErrorCode;
import com.seoulmilk.core.infrastructure.security.CustomUserDetails;
import com.seoulmilk.core.presentation.RestResponse;
import com.seoulmilk.emp.dto.request.DeleteEmpsRequest;
import com.seoulmilk.emp.dto.response.DeleteEmpResponse;
import com.seoulmilk.emp.exception.AdminErrorCode;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;

@Tag(name = "Admin", description = "관리자")
public interface DeleteEmpsSwagger {
    @Operation(
            summary = "사원 삭제 API",
            description = "사원을 삭제합니다.",
            operationId = "/v1/admin"
    )
    @ApiErrorCode({AdminErrorCode.class, GlobalErrorCode.class})
    ResponseEntity<RestResponse<DeleteEmpResponse>> delete(
            @Parameter(hidden = true)
            @AuthenticationPrincipal CustomUserDetails customUserDetails,
            DeleteEmpsRequest deleteEmpsRequest
    );
}
