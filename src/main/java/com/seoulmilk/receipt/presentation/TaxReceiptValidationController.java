package com.seoulmilk.receipt.presentation;

import com.seoulmilk.core.presentation.RestResponse;
import com.seoulmilk.receipt.application.TaxReceiptValidationService;
import com.seoulmilk.receipt.presentation.dto.request.TaxReceiptValidationRequest;
import com.seoulmilk.receipt.presentation.dto.request.TaxReceiptValidationWithAuthRequest;
import com.seoulmilk.receipt.presentation.dto.response.AdditionalAuthResponse;
import com.seoulmilk.receipt.presentation.dto.response.TaxReceiptValidationResponse;
import com.seoulmilk.receipt.presentation.swagger.TaxReceiptValidateSwagger;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/v1/receipt")
@RequiredArgsConstructor
public class TaxReceiptValidationController implements TaxReceiptValidateSwagger {
    private final TaxReceiptValidationService taxReceiptValidationService;

    @Override
    @PostMapping("/valid")
    public ResponseEntity<RestResponse<AdditionalAuthResponse>> validTaxReceipt(
            @RequestBody TaxReceiptValidationRequest request
    ){
        AdditionalAuthResponse additionalAuthResponse = taxReceiptValidationService.validateTaxReceipt(request);
        return ResponseEntity.ok(new RestResponse<>(additionalAuthResponse));
    }

    @Override
    @PostMapping("/additional")
    public ResponseEntity<RestResponse<TaxReceiptValidationResponse>> additionAuthController(
            @RequestBody TaxReceiptValidationWithAuthRequest request
    ){
        TaxReceiptValidationResponse taxReceiptValidationResponse =
                taxReceiptValidationService.validationWithAdditionalAuth(request);

        return ResponseEntity.ok(new RestResponse<>(taxReceiptValidationResponse));
    }
}
