package com.seoulmilk.emp.presentation.swagger;

import com.seoulmilk.core.infrastructure.security.CustomUserDetails;
import com.seoulmilk.core.presentation.RestResponse;
import com.seoulmilk.emp.dto.request.GrantPrivilegeRequest;
import com.seoulmilk.emp.dto.response.CreateEmpResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;

@Tag(name = "Admin", description = "관리자")
public interface CreateEmpSwagger {
    @Operation(
            summary = "사원 생성(임시) API",
            description = "사원을 생성합니다.(임시)",
            operationId = "/v1/admin"
    )
    ResponseEntity<RestResponse<CreateEmpResponse>> create(
            @Parameter(hidden = true)
            @AuthenticationPrincipal CustomUserDetails customUserDetails,
            GrantPrivilegeRequest createEmpRequest
    );
}
