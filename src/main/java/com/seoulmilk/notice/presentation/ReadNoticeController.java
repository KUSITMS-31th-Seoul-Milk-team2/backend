package com.seoulmilk.notice.presentation;

import com.seoulmilk.core.infrastructure.security.CustomUserDetails;
import com.seoulmilk.core.presentation.RestResponse;
import com.seoulmilk.notice.application.ReadNoticeService;
import com.seoulmilk.notice.dto.request.ReadNoticePaginationRequest;
import com.seoulmilk.notice.dto.response.NoticeSummaryResponse;
import com.seoulmilk.notice.dto.response.PageNoticeResponse;
import com.seoulmilk.notice.dto.response.ReadNoticeResponse;
import com.seoulmilk.notice.dto.response.ReadPaginatedResponse;
import com.seoulmilk.notice.presentation.swagger.ReadNoticeSwagger;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@Log4j2
@RequestMapping("/v1/notice")
public class ReadNoticeController implements ReadNoticeSwagger {

    private final ReadNoticeService readNoticeService;

    @GetMapping
    public ResponseEntity<RestResponse<ReadNoticeResponse>> get(
            @RequestParam Long id) {
        ReadNoticeResponse readNoticeResponse = readNoticeService.readOneNotice(id);
        return ResponseEntity.ok(new RestResponse<>(readNoticeResponse));
    }

    @GetMapping("/list")
    public ResponseEntity<RestResponse<ReadPaginatedResponse<NoticeSummaryResponse>>> getNoticesByPage(
            ReadNoticePaginationRequest readNoticePaginationRequest
    ) {

        ReadPaginatedResponse<NoticeSummaryResponse> readPaginatedResponse = readNoticeService.paginateNotices(readNoticePaginationRequest);
        return ResponseEntity.ok(new RestResponse<>(readPaginatedResponse));
    }

    @GetMapping("/my-notices")
    public ResponseEntity<RestResponse<PageNoticeResponse<NoticeSummaryResponse>>> getMyNotices(
            @AuthenticationPrincipal CustomUserDetails customUserDetails,
            @PageableDefault(size = 10, sort = "id", direction = Sort.Direction.DESC) Pageable pageable) {
        PageNoticeResponse<NoticeSummaryResponse> response = readNoticeService.getMyNotices(
                customUserDetails.getId(),
                pageable);
        return ResponseEntity.ok(new RestResponse<>(response));
    }

    @GetMapping("/search")
    public ResponseEntity<RestResponse<PageNoticeResponse<NoticeSummaryResponse>>> searchNotices(
            @RequestParam String searchType,
            @RequestParam String keyword,
            @PageableDefault(size = 10, sort = "id", direction = Sort.Direction.DESC) Pageable pageable) {
        PageNoticeResponse<NoticeSummaryResponse> response = readNoticeService.getNoticesByKeyword(searchType, keyword, pageable);
        return ResponseEntity.ok(new RestResponse<>(response));
    }
}
