package com.seoulmilk.emp.presentation.swagger;

import com.seoulmilk.core.configuration.swagger.ApiErrorCode;
import com.seoulmilk.core.exception.error.GlobalErrorCode;
import com.seoulmilk.core.infrastructure.security.CustomUserDetails;
import com.seoulmilk.core.presentation.RestResponse;
import com.seoulmilk.emp.dto.request.GrantPrivilegeRequest;
import com.seoulmilk.emp.dto.response.GrantPrivilegeResponse;
import com.seoulmilk.emp.exception.AdminErrorCode;
import com.seoulmilk.emp.exception.EmpErrorCode;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.RequestBody;

@Tag(name = "Admin", description = "관리자")
public interface GrantPrivilegeSwagger {
    @Operation(
            summary = "권한 부여 API",
            description = "사원에게 권한을 부여합니다.",
            operationId = "/v1/emp/grant-privilege"
    )
    @ApiErrorCode({GlobalErrorCode.class, EmpErrorCode.class, AdminErrorCode.class})
    ResponseEntity<RestResponse<GrantPrivilegeResponse>> grantPrivilege(
            @Parameter(hidden = true)
            @AuthenticationPrincipal CustomUserDetails customUserDetails,
            @RequestBody GrantPrivilegeRequest grantPrivilegeRequest
    );
}
