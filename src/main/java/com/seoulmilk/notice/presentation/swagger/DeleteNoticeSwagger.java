package com.seoulmilk.notice.presentation.swagger;

import com.seoulmilk.core.configuration.swagger.ApiErrorCode;
import com.seoulmilk.core.exception.error.GlobalErrorCode;
import com.seoulmilk.core.infrastructure.security.CustomUserDetails;
import com.seoulmilk.core.presentation.RestResponse;
import com.seoulmilk.notice.dto.request.DeleteNoticeRequest;
import com.seoulmilk.notice.exception.NoticeErrorCode;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.RequestBody;

@Tag(name = "Notice", description = "공지사항")
public interface DeleteNoticeSwagger {
    @Operation(
            summary = "공지사항 삭제 API",
            description = "공지사항을 삭제합니다.",
            operationId = "/v1/notice"
    )
    @ApiErrorCode({GlobalErrorCode.class, NoticeErrorCode.class})
    ResponseEntity<RestResponse<Boolean>> delete(
            @Parameter(hidden = true)
            @AuthenticationPrincipal CustomUserDetails customUserDetails,
            @RequestBody @Valid DeleteNoticeRequest deleteNoticeRequest);
}
