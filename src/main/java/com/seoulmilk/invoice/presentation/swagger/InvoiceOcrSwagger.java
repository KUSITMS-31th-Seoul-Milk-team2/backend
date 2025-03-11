package com.seoulmilk.invoice.presentation.swagger;

import com.seoulmilk.core.configuration.swagger.ApiErrorCode;
import com.seoulmilk.core.exception.error.GlobalErrorCode;
import com.seoulmilk.core.infrastructure.security.CustomUserDetails;
import com.seoulmilk.core.presentation.RestResponse;
import com.seoulmilk.emp.exception.EmpErrorCode;
import com.seoulmilk.invoice.application.exception.InvoiceErrorCode;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@Tag(name = "InvoiceOcr", description = "영수증 OCR")
public interface InvoiceOcrSwagger {
    @Operation(
            summary = "세금계산서 OCR API",
            description = "세금계산서를 OCR 합니다.",
            operationId = "/v1/invoice"
    )
    @ApiErrorCode({GlobalErrorCode.class, InvoiceErrorCode.class, EmpErrorCode.class})
    ResponseEntity<RestResponse<Boolean>> uploadMultipleFiles(
            @Parameter(hidden = true)
            @AuthenticationPrincipal CustomUserDetails customUserDetails,
            @RequestPart("files") List<MultipartFile> files);
}

