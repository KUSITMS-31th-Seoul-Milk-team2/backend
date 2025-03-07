package com.seoulmilk.invoice.presentation;

import com.seoulmilk.core.infrastructure.security.CustomUserDetails;
import com.seoulmilk.invoice.application.OpenFeignService;
import com.seoulmilk.core.presentation.RestResponse;
import com.seoulmilk.invoice.application.OcrEventPublisher;
import com.seoulmilk.receipt.dto.request.OcrValidationRequest;
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
import java.util.stream.Collectors;

@RestController
@Log4j2
@RequiredArgsConstructor
@RequestMapping("/v1/invoice")
public class InvoiceOcrController {
    private final OpenFeignService openFeignService;
    private final OcrEventPublisher ocrEventPublisher;

    @PostMapping
    public ResponseEntity<RestResponse<Boolean>> uploadMultipleFiles(
            @AuthenticationPrincipal CustomUserDetails customUserDetails,
            @RequestPart("files") List<MultipartFile> files) {
        List<CompletableFuture<OcrValidationRequest>> futures = files.stream()
                .map(file -> CompletableFuture.supplyAsync(() -> openFeignService.processImg(customUserDetails.getId(),file)))
                .toList();

        CompletableFuture<Void> allDone = CompletableFuture.allOf(futures.toArray(new CompletableFuture[0]));

        CompletableFuture<List<OcrValidationRequest>> combined = allDone.thenApply(v ->
                futures.stream()
                        .map(CompletableFuture::join)
                        .collect(Collectors.toList())
        );

        combined.thenAccept(
                ocrValidationRequests -> {
                    log.info("이벤트 발송");
                    ocrEventPublisher.publish(ocrValidationRequests);
                }
        );
        return ResponseEntity.ok(new RestResponse<>(true));
    }
}

