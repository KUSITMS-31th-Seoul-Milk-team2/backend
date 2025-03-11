package com.seoulmilk.emp.presentation.swagger;

import com.seoulmilk.core.configuration.swagger.ApiErrorCode;
import com.seoulmilk.core.exception.error.GlobalErrorCode;
import com.seoulmilk.core.infrastructure.security.CustomUserDetails;
import com.seoulmilk.core.presentation.RestResponse;
import com.seoulmilk.emp.dto.request.UpdateHometaxInfoRequest;
import com.seoulmilk.emp.dto.response.UpdateHometaxInfoResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;

@Tag(name = "MyPage", description = "마이페이지")
public interface UpdateValidationInfoSwagger {

    @Operation(
            summary = "홈택스 정보 수정 API",
            description = "홈택스 정보를 수정합니다.",
            operationId = "/v1/emp/hometax"
    )
    @ApiErrorCode({GlobalErrorCode.class})
    ResponseEntity<RestResponse<UpdateHometaxInfoResponse>> updateHometaxInfo(
            @Parameter(hidden = true)
            @AuthenticationPrincipal CustomUserDetails customUserDetails,
            UpdateHometaxInfoRequest updateHometaxInfoRequest
    );
}
