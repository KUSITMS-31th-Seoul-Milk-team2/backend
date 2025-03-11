package com.seoulmilk.invoice.presentation;

import com.seoulmilk.core.infrastructure.security.CustomUserDetails;
import com.seoulmilk.core.presentation.RestResponse;
import com.seoulmilk.invoice.application.OcrEventPublisher;
import com.seoulmilk.invoice.application.OpenFeignService;
import com.seoulmilk.invoice.presentation.swagger.InvoiceOcrSwagger;
import com.seoulmilk.receipt.dto.request.OcrValidationRequest;
import feign.FeignException;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.concurrent.CompletableFuture;

@RestController
@Log4j2
@RequiredArgsConstructor
@RequestMapping("/v1/invoice")
public class InvoiceOcrController implements InvoiceOcrSwagger {
    private final OpenFeignService openFeignService;
    private final OcrEventPublisher ocrEventPublisher;

    @PostMapping
    public ResponseEntity<RestResponse<Boolean>> uploadMultipleFiles(
            @AuthenticationPrincipal CustomUserDetails customUserDetails,
            @RequestPart("files") List<MultipartFile> files) {

        List<OcrValidationRequest> results = files.stream()
                .map(file -> {
                    try {
                        return openFeignService.processImg(customUserDetails.getId(), file);
                    } catch (FeignException e) {
                        log.error("OCR 처리 실패: {}", e.contentUTF8());
                        return null;
                    }
                })
                .toList();

        CompletableFuture.runAsync(() -> {
            List<OcrValidationRequest> validResults = results.stream()
                    .filter(result -> result != null)
                    .toList();
            log.info("validResults: {}", validResults);
            ocrEventPublisher.publish(validResults);
        });


        return ResponseEntity.ok(new RestResponse<>(true));
    }
}

