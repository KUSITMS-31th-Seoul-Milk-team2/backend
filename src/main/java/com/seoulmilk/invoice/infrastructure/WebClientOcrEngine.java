package com.seoulmilk.invoice.infrastructure;

import com.seoulmilk.invoice.domain.service.OcrEngine;
import com.seoulmilk.invoice.dto.response.OcrResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.http.MediaType;
import org.springframework.http.client.MultipartBodyBuilder;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.BodyInserters;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

@Slf4j
@Service
@RequiredArgsConstructor
public class WebClientOcrEngine implements OcrEngine {

    private final WebClient webClient;

    @Value("${clova.ocr.ocr-invoke-url}")
    private String ocrInvokeUrl;

    @Override
    public Mono<OcrResponse> extractText(String message, byte[] fileBytes, String fileName) {
        MultipartBodyBuilder bodyBuilder = new MultipartBodyBuilder();
        bodyBuilder.part("message", message);
        bodyBuilder.part("file", new ByteArrayResource(fileBytes) {
            @Override
            public String getFilename() {
                return fileName;
            }
        });

        return webClient.post()
                .uri(ocrInvokeUrl)
                .contentType(MediaType.MULTIPART_FORM_DATA)
                .body(BodyInserters.fromMultipartData(bodyBuilder.build()))
                .retrieve()
                .bodyToMono(OcrResponse.class)
                .doOnSuccess(response -> log.info("OCR 요청 완료"))
                .doOnError(error -> log.error("OCR 요청 실패", error));
    }
}
