package com.seoulmilk.receipt.presentation;

import com.seoulmilk.core.presentation.RestResponse;
import com.seoulmilk.receipt.application.TaxReceiptValidationService;
import com.seoulmilk.receipt.application.ValidationWithEasyCodefService;
import com.seoulmilk.receipt.presentation.dto.request.TaxReceiptValidationRequest;
import com.seoulmilk.receipt.presentation.dto.request.TaxReceiptValidationWithAuthRequest;
import com.seoulmilk.receipt.presentation.dto.response.AdditionalAuthResponse;
import com.seoulmilk.receipt.presentation.dto.response.TaxReceiptValidationResponse;
import com.seoulmilk.receipt.presentation.swagger.TaxReceiptValidateSwagger;
import io.codef.api.dto.EasyCodefResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/v1/receipt")
@RequiredArgsConstructor
@Log4j2
public class TaxReceiptValidationController implements TaxReceiptValidateSwagger {
    private final TaxReceiptValidationService taxReceiptValidationService;
    private final ValidationWithEasyCodefService validationWithEasyCodefService;
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

    @Override
    @PostMapping("/multiple-validation")
    public ResponseEntity<RestResponse<EasyCodefResponse>> validateTaxReceipts(
            @RequestBody List<TaxReceiptValidationRequest> requests
    ){
        log.info("[validateTaxReceipts] 컨트롤러 작동");
        return ResponseEntity.ok(new RestResponse<>(validationWithEasyCodefService.getAdditionalAuthResponse(requests)));
    }

    @Override
    @PostMapping("/multiple-addition")
    public ResponseEntity<RestResponse<List<EasyCodefResponse>>> multipleAdditionAuthController(
            @RequestParam String transactionId
    ){
        log.info("[multipleAdditionAuthController] 컨트롤러 작동");
        return ResponseEntity.ok(new RestResponse<>(validationWithEasyCodefService.getMultipleTaxReceiptValidationResponse(transactionId)));
    }
}
