package com.seoulmilk.receipt.presentation;

import com.seoulmilk.core.infrastructure.security.CustomUserDetails;
import com.seoulmilk.core.presentation.RestResponse;
import com.seoulmilk.receipt.application.TaxReceiptValidationService;
import com.seoulmilk.receipt.dto.request.OcrValidationRequest;
import com.seoulmilk.receipt.dto.request.TaxReceiptValidationRequest;
import com.seoulmilk.receipt.presentation.dto.response.AdditionalAuthResponse;
import com.seoulmilk.receipt.presentation.dto.response.TaxReceiptValidationResponse;
import com.seoulmilk.receipt.presentation.swagger.TaxReceiptValidateSwagger;
import io.swagger.v3.oas.annotations.Hidden;
import io.swagger.v3.oas.annotations.Parameter;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/v1/receipt")
@RequiredArgsConstructor
@Log4j2
public class TaxReceiptValidationController implements TaxReceiptValidateSwagger {
    private final TaxReceiptValidationService taxReceiptValidationService;

    @Override
    @PostMapping("/validation")
    public ResponseEntity<RestResponse<AdditionalAuthResponse>> validateTaxReceipts(
            @RequestBody List<TaxReceiptValidationRequest> requests
    ){
        log.info("[validateTaxReceipts] 컨트롤러 작동");
        return ResponseEntity.ok(
                new RestResponse<>(taxReceiptValidationService.requestAdditionalAuthentication(requests))
        );
    }

    @Override
    @PostMapping("/addition")
    public ResponseEntity<RestResponse<List<TaxReceiptValidationResponse>>> multipleAdditionAuthController(
            @Parameter(hidden = true)
            @AuthenticationPrincipal CustomUserDetails customUserDetails,
            @RequestParam String transactionId,
            @RequestBody List<OcrValidationRequest> requests
    ){
        log.info("[multipleAdditionAuthController] 컨트롤러 작동");
        return ResponseEntity.ok(
                new RestResponse<>(taxReceiptValidationService.retrieveValidatedTaxReceipts(customUserDetails.getId(), requests, transactionId))
        );
    }

    @Override
    @PostMapping("/upload/addition")
    public ResponseEntity<RestResponse<List<TaxReceiptValidationResponse>>> multipleAdditionAuthController(
            @Parameter(hidden = true)
            @AuthenticationPrincipal CustomUserDetails customUserDetails
    ){
        log.info("[multipleAdditionAuthController] 컨트롤러 작동");
        return ResponseEntity.ok(
                new RestResponse<>(taxReceiptValidationService.retrieveValidatedTaxReceiptsWithTransactionId(customUserDetails))
        );
    }


}
