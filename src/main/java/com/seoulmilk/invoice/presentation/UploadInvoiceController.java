package com.seoulmilk.invoice.presentation;

import com.seoulmilk.core.presentation.RestResponse;
import com.seoulmilk.invoice.application.OpenFeignService;
import com.seoulmilk.invoice.dto.response.OcrResponse;
import com.seoulmilk.invoice.presentation.swagger.UploadInvoiceSwagger;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

@RestController
@RequiredArgsConstructor
@Log4j2
@RequestMapping("/v1/invoice")
public class UploadInvoiceController implements UploadInvoiceSwagger {

    public final OpenFeignService openFeignService;

    @PostMapping
    public ResponseEntity<RestResponse<OcrResponse>> upload(
            @RequestParam("file") MultipartFile file
    ) {
        OcrResponse ocrResponse =  openFeignService.processImg(file);
        return ResponseEntity.ok(new RestResponse<>(ocrResponse));
    }
}
