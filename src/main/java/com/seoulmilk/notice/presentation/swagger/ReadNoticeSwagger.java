package com.seoulmilk.notice.presentation.swagger;

import com.seoulmilk.core.configuration.swagger.ApiErrorCode;
import com.seoulmilk.core.exception.error.GlobalErrorCode;
import com.seoulmilk.core.infrastructure.security.CustomUserDetails;
import com.seoulmilk.core.presentation.RestResponse;
import com.seoulmilk.emp.exception.EmpErrorCode;
import com.seoulmilk.notice.dto.request.ReadNoticePaginationRequest;
import com.seoulmilk.notice.dto.response.NoticeSummaryResponse;
import com.seoulmilk.notice.dto.response.PageNoticeResponse;
import com.seoulmilk.notice.dto.response.ReadNoticeResponse;
import com.seoulmilk.notice.dto.response.ReadPaginatedResponse;
import com.seoulmilk.notice.exception.NoticeErrorCode;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springdoc.core.annotations.ParameterObject;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.RequestParam;

@Tag(name = "Notice", description = "공지사항")
public interface ReadNoticeSwagger {
    @Operation(
            summary = "공지사항 조회 API",
            description = "특정 공지사항을 조회합니다.",
            operationId = "/v1/notice?id={id}"
    )
    @ApiErrorCode({GlobalErrorCode.class, NoticeErrorCode.class, EmpErrorCode.class})
    ResponseEntity<RestResponse<ReadNoticeResponse>> get(
            @Schema(description = "공지사항 ID", example = "1")
            @RequestParam Long id);

    @Operation(
            summary = "공지사항 목록 조회 API",
            description = "공지사항 목록을 조회합니다.",
            operationId = "/v1/notice/list"
    )
    @ApiErrorCode({GlobalErrorCode.class, EmpErrorCode.class})
    ResponseEntity<RestResponse<ReadPaginatedResponse<NoticeSummaryResponse>>> getNoticesByPage(
            @ParameterObject
            ReadNoticePaginationRequest readNoticePaginationRequest);

    @Operation(
            summary = "내가 쓴 공지사항 목록 조회 API",
            description = "내가 쓴 공지사항 목록을 조회합니다.",
            operationId = "/v1/notice/my-notices"
    )
    @ApiErrorCode({GlobalErrorCode.class, EmpErrorCode.class})
    ResponseEntity<RestResponse<PageNoticeResponse<NoticeSummaryResponse>>> getMyNotices(
            @Parameter(hidden = true) @AuthenticationPrincipal CustomUserDetails customUserDetails,
            @ParameterObject
            @PageableDefault(size = 10, sort = "id", direction = Sort.Direction.DESC) Pageable pageable);

    @Operation(
            summary = "공지사항 조건 필터링 조회 API",
            description = "공지사항을 조건에 맞게 필터링하여 조회합니다.",
            operationId = "/v1/notice/search"
    )
    @ApiErrorCode({GlobalErrorCode.class, EmpErrorCode.class})
    ResponseEntity<RestResponse<PageNoticeResponse<NoticeSummaryResponse>>> searchNotices(
            @Schema(description = "검색 타입", example = "all, title_and_content, author")
            @RequestParam String searchType,
            @Schema(description = "검색 키워드", example = "검색할 키워드")
            @RequestParam String keyword,
            @ParameterObject
            @PageableDefault(size = 10, sort = "id", direction = Sort.Direction.DESC) Pageable pageable);
}
