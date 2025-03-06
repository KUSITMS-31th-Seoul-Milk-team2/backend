package com.seoulmilk.invoice.presentation;

import com.seoulmilk.core.infrastructure.security.CustomUserDetails;
import com.seoulmilk.core.presentation.RestResponse;
import com.seoulmilk.invoice.application.WebClientOcrService;
import com.seoulmilk.invoice.dto.response.OcrResponse;
import com.seoulmilk.invoice.presentation.swagger.UploadInvoiceSwagger;
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
import reactor.core.publisher.Flux;

import java.util.List;

@RestController
@RequiredArgsConstructor
@Log4j2
@RequestMapping("/v1/invoice")
public class UploadInvoiceController implements UploadInvoiceSwagger {

    public final WebClientOcrService webClientOcrService;

    @Override
    @PostMapping(consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<RestResponse<Flux<OcrResponse>>> upload(
            @AuthenticationPrincipal CustomUserDetails customUserDetails,
            @RequestPart("files") List<MultipartFile> files) {

        for (MultipartFile file : files) {
            webClientOcrService.processFile(file)
                    .subscribe(
                            response -> log.info("OCR 결과: {}", response),
                            error -> log.error("OCR 처리 중 오류 발생", error)
                    );
        }
        // TODO: 파일 OCR 처리 후 국세청 검증 결과를 반환해야함.
        return ResponseEntity.ok(new RestResponse<>(Flux.empty()));
    }
}
