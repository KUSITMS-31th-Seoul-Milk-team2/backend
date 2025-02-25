package com.seoulmilk.invoice.presentation.swagger;

import com.seoulmilk.core.configuration.swagger.ApiErrorCode;
import com.seoulmilk.core.exception.error.GlobalErrorCode;
import com.seoulmilk.core.presentation.RestResponse;
import com.seoulmilk.invoice.dto.response.OcrResponse;
import com.seoulmilk.invoice.exception.InvoiceErrorCode;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.multipart.MultipartFile;

@Tag(name = "Invoice", description = "세금계산서")
public interface UploadInvoiceSwagger {

    @Operation(
            summary = "세금계산서 업로드 API",
            description = "사용자가 세금계산서 이미지를 업로드하여 처리합니다.",
            operationId = "/v1/invoice"
    )
    @ApiErrorCode({GlobalErrorCode.class, InvoiceErrorCode.class})
    ResponseEntity<RestResponse<OcrResponse>> upload(
            @Parameter(
                    name = "file",
                    description = "업로드할 세금계산서 이미지 파일",
                    required = true,
                    content = @Content(mediaType = MediaType.MULTIPART_FORM_DATA_VALUE),
                    schema = @Schema(type = "string", format = "binary")
            ) MultipartFile file
    );
}
