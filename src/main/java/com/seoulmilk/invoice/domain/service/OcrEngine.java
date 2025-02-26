package com.seoulmilk.invoice.domain.service;

import com.seoulmilk.invoice.dto.response.OcrResponse;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.multipart.MultipartFile;

public interface OcrEngine {
    OcrResponse extractText(
            @RequestPart("message") String message,
            @RequestPart("file") MultipartFile file
    );
}
