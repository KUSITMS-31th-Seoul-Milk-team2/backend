package com.seoulmilk.receipt.presentation.swagger;

import com.seoulmilk.core.configuration.swagger.ApiErrorCode;
import com.seoulmilk.receipt.exception.ExcelErrorCode;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.ResponseEntity;

import java.io.IOException;

@Tag(name = "Excel", description = "유효한 세금계산서 전체 엑셀 다운로드")
public interface ValidatedTaxReceiptExcelDownloadSwagger {
    @Operation(
            summary = "유효한 세금계산서 전체 엑셀 다운로드 API",
            description = "유효한 세금계산서 전체를 엑셀로 다운로드합니다.",
            operationId = "/v1/receipt/download"
    )
    @ApiErrorCode({ExcelErrorCode.class})
    ResponseEntity<byte[]> downloadValidatedTaxReceiptExcel();
}
