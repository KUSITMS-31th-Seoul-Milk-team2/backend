package com.seoulmilk.notice.presentation;

import com.seoulmilk.core.infrastructure.security.CustomUserDetails;
import com.seoulmilk.core.presentation.RestResponse;
import com.seoulmilk.notice.application.PostNoticeService;
import com.seoulmilk.notice.dto.request.PostNoticeRequest;
import com.seoulmilk.notice.dto.response.PostNoticeResponse;
import com.seoulmilk.notice.presentation.swagger.PostNoticeSwagger;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

@RestController
@RequiredArgsConstructor
@RequestMapping("/v1/notice")
public class PostNoticeController implements PostNoticeSwagger {

    private final PostNoticeService postNoticeService;

    @PostMapping(consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<RestResponse<PostNoticeResponse>> post(
            @AuthenticationPrincipal CustomUserDetails customUserDetails,
            @RequestPart @Valid PostNoticeRequest postNoticeRequest,
            @RequestPart(value = "file", required = false) MultipartFile file
    ) {
        PostNoticeResponse postNoticeResponse = postNoticeService.post(customUserDetails, postNoticeRequest, file);
        return ResponseEntity.ok(new RestResponse<>(postNoticeResponse));
    }
}
