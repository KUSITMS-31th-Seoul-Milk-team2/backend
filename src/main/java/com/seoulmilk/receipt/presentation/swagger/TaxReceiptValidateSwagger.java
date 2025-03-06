package com.seoulmilk.receipt.presentation.swagger;

import com.seoulmilk.auth.exception.AuthenticationErrorCode;

import com.seoulmilk.core.configuration.swagger.ApiErrorCode;
import com.seoulmilk.core.exception.error.GlobalErrorCode;
import com.seoulmilk.core.presentation.RestResponse;
import com.seoulmilk.receipt.exception.ReceiptErrorCode;
import com.seoulmilk.receipt.dto.request.TaxReceiptValidationRequest;
import com.seoulmilk.receipt.presentation.dto.response.AdditionalAuthResponse;
import com.seoulmilk.receipt.presentation.dto.response.TaxReceiptValidationResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;

@Tag(name = "Valid", description = "세금계산서 검증 프로세스")
public interface TaxReceiptValidateSwagger {
    @Operation(
            summary = "세금계산서 발급사실 검증 API",
            description = "여러 정보들을 입력하여 세금계산서 검증을 실시합니다",
            operationId = "/v1/receipt/validation"
    )
    @ApiErrorCode({GlobalErrorCode.class, AuthenticationErrorCode.class, ReceiptErrorCode.class})
    ResponseEntity<RestResponse<AdditionalAuthResponse>> validateTaxReceipts(
            @RequestBody List<TaxReceiptValidationRequest> requestList
    );

    @Operation(
            summary = "세금계산서 발급 사실 추가인증 API",
            description = "세금계산서 검증시 추가 인증을 실시합니다.",
            operationId = "/v1/receipt/addition"
    )
    @ApiErrorCode({GlobalErrorCode.class, AuthenticationErrorCode.class, ReceiptErrorCode.class})
    ResponseEntity<RestResponse<List<TaxReceiptValidationResponse>>> multipleAdditionAuthController(
            @RequestParam String transactionId
    );

}
