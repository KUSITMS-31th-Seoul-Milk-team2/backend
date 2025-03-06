package com.seoulmilk.notice.presentation;

import com.seoulmilk.core.infrastructure.security.CustomUserDetails;
import com.seoulmilk.core.presentation.RestResponse;
import com.seoulmilk.notice.application.DeleteNoticeService;
import com.seoulmilk.notice.dto.request.DeleteNoticeRequest;
import com.seoulmilk.notice.presentation.swagger.DeleteNoticeSwagger;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/v1/notice")
@Log4j2
public class DeleteNoticeController implements DeleteNoticeSwagger {

    private final DeleteNoticeService deleteNoticeService;

    @DeleteMapping
    public ResponseEntity<RestResponse<Boolean>> delete(
            @AuthenticationPrincipal CustomUserDetails customUserDetails,
            @RequestBody @Valid DeleteNoticeRequest deleteNoticeRequest) {
        deleteNoticeService.delete(customUserDetails, deleteNoticeRequest);
        return ResponseEntity.ok(new RestResponse<>(true));
    }
}
