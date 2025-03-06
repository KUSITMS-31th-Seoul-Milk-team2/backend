package com.seoulmilk.invoice.domain.service;

import com.seoulmilk.invoice.dto.response.OcrResponse;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestPart;
import reactor.core.publisher.Mono;

public interface OcrEngine {

    @PostMapping(value = "/extract", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    Mono<OcrResponse> extractText(
            Long empPk,
            @RequestPart("message") String message,
            @RequestPart("file") byte[] fileByte,
            String fileName
    );
}
