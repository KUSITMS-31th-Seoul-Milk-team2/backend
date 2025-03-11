package com.seoulmilk.emp.presentation.swagger;

import com.seoulmilk.core.configuration.swagger.ApiErrorCode;
import com.seoulmilk.core.infrastructure.security.CustomUserDetails;
import com.seoulmilk.core.presentation.RestResponse;
import com.seoulmilk.emp.dto.response.ReadEmpResponse;
import com.seoulmilk.emp.dto.response.ReadEmpsResponse;
import com.seoulmilk.emp.exception.AdminErrorCode;
import com.seoulmilk.emp.exception.EmpErrorCode;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.RequestParam;

@Tag(name = "Admin", description = "관리자")
public interface ReadEmpSwagger {
    @Operation(
            summary = "전체 사원 조회 API",
            description = "전체 사원을 조회합니다.",
            operationId = "/v1/admin/all"
    )
    @ApiErrorCode({AdminErrorCode.class})
    ResponseEntity<RestResponse<ReadEmpsResponse>> read(
            @Parameter(hidden = true)
            @AuthenticationPrincipal CustomUserDetails customUserDetails
    );

    @Operation(
            summary = "특정 사원 조회 API",
            description = "사원을 조회합니다.",
            operationId = "/v1/admin"
    )
    @ApiErrorCode({AdminErrorCode.class, EmpErrorCode.class})
    ResponseEntity<RestResponse<ReadEmpResponse>> readOneEmp(
            @Parameter(hidden = true)
            @AuthenticationPrincipal CustomUserDetails customUserDetails,
            @RequestParam String name
    );
}
