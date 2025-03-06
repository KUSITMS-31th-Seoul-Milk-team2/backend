package com.seoulmilk.notice.presentation;

import com.seoulmilk.core.infrastructure.security.CustomUserDetails;
import com.seoulmilk.core.presentation.RestResponse;
import com.seoulmilk.notice.application.UpdateNoticeService;
import com.seoulmilk.notice.dto.request.UpdateNoticeRequest;
import com.seoulmilk.notice.presentation.swagger.UpdateNoticeSwagger;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

@RestController
@RequestMapping("/v1/notice")
@RequiredArgsConstructor
public class UpdateNoticeController implements UpdateNoticeSwagger {
    private final UpdateNoticeService updateNoticeService;

    @PutMapping
    public ResponseEntity<RestResponse<Boolean>> update(
            @AuthenticationPrincipal CustomUserDetails customUserDetails,
            @RequestPart @Valid UpdateNoticeRequest updateNoticeRequest,
            @RequestPart(value = "file", required = false) MultipartFile file
    ) {
        updateNoticeService.updateNotice(customUserDetails, updateNoticeRequest, file);
        return ResponseEntity.ok(new RestResponse<>(true));
    }
}
