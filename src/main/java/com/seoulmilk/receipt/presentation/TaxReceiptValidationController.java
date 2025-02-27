package com.seoulmilk.receipt.presentation;

import com.seoulmilk.core.presentation.RestResponse;
import com.seoulmilk.receipt.application.TaxReceiptValidationService;
import com.seoulmilk.receipt.presentation.dto.request.TaxReceiptValidationRequest;
import com.seoulmilk.receipt.presentation.dto.request.TaxReceiptValidationWithAuthRequest;
import com.seoulmilk.receipt.presentation.dto.response.AdditionalAuthResponse;
import com.seoulmilk.receipt.presentation.dto.response.TaxReceiptValidationResponse;
import com.seoulmilk.receipt.presentation.swagger.TaxReceiptValidateSwagger;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/v1/receipt")
@RequiredArgsConstructor
@Log4j2
public class TaxReceiptValidationController implements TaxReceiptValidateSwagger {
    private final TaxReceiptValidationService taxReceiptValidationService;

    @Override
    @PostMapping("/validation")
    public ResponseEntity<RestResponse<AdditionalAuthResponse>> validTaxReceipt(
            @RequestBody TaxReceiptValidationRequest request
    ){
        log.info("[validTaxReceipt] 컨트롤러 작동");
        AdditionalAuthResponse additionalAuthResponse = taxReceiptValidationService.validateTaxReceipt(request);
        return ResponseEntity.ok(new RestResponse<>(additionalAuthResponse));
    }

    @Override
    @PostMapping("/addition")
    public ResponseEntity<RestResponse<TaxReceiptValidationResponse>> additionAuthController(
            @RequestBody TaxReceiptValidationWithAuthRequest request
    ){
        log.info("[additionAuthController] 컨트롤러 작동");
        TaxReceiptValidationResponse taxReceiptValidationResponse =
                taxReceiptValidationService.validationWithAdditionalAuth(request);

        return ResponseEntity.ok(new RestResponse<>(taxReceiptValidationResponse));
    }
}
