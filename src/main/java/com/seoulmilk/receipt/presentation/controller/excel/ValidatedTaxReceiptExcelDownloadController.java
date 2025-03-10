package com.seoulmilk.receipt.presentation.controller.excel;

import com.seoulmilk.receipt.application.ValidatedTaxReceiptExcelDownloadService;
import com.seoulmilk.receipt.presentation.swagger.ValidatedTaxReceiptExcelDownloadSwagger;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.io.IOException;

@RestController
@RequiredArgsConstructor
@RequestMapping("/v1/receipt")
public class ValidatedTaxReceiptExcelDownloadController implements ValidatedTaxReceiptExcelDownloadSwagger {
    private final ValidatedTaxReceiptExcelDownloadService validatedTaxReceiptExcelDownloadService;

    @GetMapping("/download")
    public ResponseEntity<byte[]> downloadValidatedTaxReceiptExcel() {
        return ResponseEntity.ok().body(validatedTaxReceiptExcelDownloadService.exportValidatedTaxReceiptToExcel());
    }
}
