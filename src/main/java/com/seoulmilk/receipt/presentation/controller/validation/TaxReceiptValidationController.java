package com.seoulmilk.receipt.presentation.controller.validation;

import com.seoulmilk.core.infrastructure.security.CustomUserDetails;
import com.seoulmilk.core.presentation.RestResponse;
import com.seoulmilk.receipt.application.TaxReceiptValidationService;
import com.seoulmilk.receipt.dto.request.OcrValidationRequest;
import com.seoulmilk.receipt.presentation.dto.request.ValidationRequest;
import com.seoulmilk.receipt.presentation.dto.response.AdditionalAuthResponse;
import com.seoulmilk.receipt.presentation.dto.response.TaxReceiptValidationResponse;
import com.seoulmilk.receipt.presentation.swagger.TaxReceiptValidateSwagger;
import io.swagger.v3.oas.annotations.Parameter;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/v1/receipt")
@RequiredArgsConstructor
@Log4j2
public class TaxReceiptValidationController implements TaxReceiptValidateSwagger {
    private final TaxReceiptValidationService taxReceiptValidationService;

    @Override
    @PostMapping("/validation")
    public ResponseEntity<RestResponse<String>> validateTaxReceipts(
            @Parameter(hidden = true)
            @AuthenticationPrincipal CustomUserDetails customUserDetails,
            @RequestBody List<ValidationRequest> requests
    ){
        log.info("[validateTaxReceipts] 컨트롤러 작동");
        return ResponseEntity.ok(
                new RestResponse<>(
                        taxReceiptValidationService.requestAdditionalAuthentication(customUserDetails, requests)
                )
        );
    }

    @Override
    @PostMapping("/addition")
    public ResponseEntity<RestResponse<Boolean>> multipleAdditionAuthController(
            @Parameter(hidden = true)
            @AuthenticationPrincipal CustomUserDetails customUserDetails,
            @RequestParam String transactionId,
            @RequestBody List<Long> inValidReceiptPks
    ){
        log.info("[multipleAdditionAuthController] 컨트롤러 작동");
        return ResponseEntity.ok(
                new RestResponse<>(taxReceiptValidationService.retrieveValidatedTaxReceipts(
                        customUserDetails, inValidReceiptPks, transactionId)
                )
        );
    }

    @Override
    @PostMapping("/upload/addition")
    public ResponseEntity<RestResponse<Boolean>> uploadAdditionAuthController(
            @Parameter(hidden = true)
            @AuthenticationPrincipal CustomUserDetails customUserDetails
    ){
        log.info("[uploadAdditionAuthController] 컨트롤러 작동");
        return ResponseEntity.ok(
                new RestResponse<>(taxReceiptValidationService.retrieveValidatedTaxReceiptsWithTransactionId(customUserDetails))
        );
    }


}
