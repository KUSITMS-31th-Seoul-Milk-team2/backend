package com.seoulmilk.notice.presentation.swagger;

import com.seoulmilk.core.configuration.swagger.ApiErrorCode;
import com.seoulmilk.core.exception.error.GlobalErrorCode;
import com.seoulmilk.core.infrastructure.security.CustomUserDetails;
import com.seoulmilk.core.presentation.RestResponse;
import com.seoulmilk.emp.exception.EmpErrorCode;
import com.seoulmilk.notice.dto.request.UpdateNoticeRequest;
import com.seoulmilk.notice.exception.NoticeErrorCode;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.multipart.MultipartFile;

@Tag(name = "Notice", description = "공지사항")
public interface UpdateNoticeSwagger {
 @Operation(
            summary = "공지사항 수정 API",
            description = "공지사항을 수정합니다.",
            operationId = "/v1/notice"
 )
 @ApiErrorCode({GlobalErrorCode.class, EmpErrorCode.class, NoticeErrorCode.class})
 ResponseEntity<RestResponse<Boolean>> update(
         @Parameter(hidden = true)
         @AuthenticationPrincipal CustomUserDetails customUserDetails,
         @RequestPart @Valid UpdateNoticeRequest updateNoticeRequest,
         @RequestPart(value = "file", required = false) MultipartFile file
 );
}
