package com.seoulmilk.notice.presentation;

import com.seoulmilk.core.infrastructure.security.CustomUserDetails;
import com.seoulmilk.core.presentation.RestResponse;
import com.seoulmilk.notice.application.ReadNoticeService;
import com.seoulmilk.notice.dto.response.NoticeSummaryResponse;
import com.seoulmilk.notice.dto.response.PageResponse;
import com.seoulmilk.notice.dto.response.ReadNoticeResponse;
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
@RequestMapping("/v2/notice")
public class ReadNoticeController {

    private final ReadNoticeService readNoticeService;

    @GetMapping
    public ResponseEntity<RestResponse<ReadNoticeResponse>> get(
            @RequestParam Long id) {
        ReadNoticeResponse readNoticeResponse = readNoticeService.readOneNotice(id);
        return ResponseEntity.ok(new RestResponse<>(readNoticeResponse));
    }

    @GetMapping("/list")
    public ResponseEntity<RestResponse<PageResponse<NoticeSummaryResponse>>> getNoticesByPage(
            @PageableDefault(size = 10, sort = "id", direction = Sort.Direction.DESC) Pageable pageable
    ) {

        PageResponse<NoticeSummaryResponse> readPaginatedResponse = readNoticeService.getNoticesByPage(pageable);
        return ResponseEntity.ok(new RestResponse<>(readPaginatedResponse));
    }

    @GetMapping("/my-notices")
    public ResponseEntity<RestResponse<PageResponse<NoticeSummaryResponse>>> getMyNotices(
            @AuthenticationPrincipal CustomUserDetails customUserDetails,
            @PageableDefault(size = 10, sort = "id", direction = Sort.Direction.DESC) Pageable pageable) {
        PageResponse<NoticeSummaryResponse> response = readNoticeService.getMyNotices(
                customUserDetails.getId(),
                pageable);
        return ResponseEntity.ok(new RestResponse<>(response));
    }

    @GetMapping("/search")
    public ResponseEntity<RestResponse<PageResponse<NoticeSummaryResponse>>> searchNotices(
            @RequestParam String searchType,
            @RequestParam String keyword,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size
    ) {
        PageResponse<NoticeSummaryResponse> response = readNoticeService.getNoticesByKeyword(searchType, keyword, page, size);
        return ResponseEntity.ok(new RestResponse<>(response));
    }
}
