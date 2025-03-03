package com.seoulmilk.receipt.presentation;

import com.seoulmilk.core.presentation.RestResponse;
import com.seoulmilk.receipt.application.TaxReceiptValidationServiceImpl;
import com.seoulmilk.receipt.presentation.dto.request.TaxReceiptValidationRequest;
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
    private final TaxReceiptValidationServiceImpl taxReceiptValidationService;

    @Override
    @PostMapping("/validation")
    public ResponseEntity<RestResponse<EasyCodefResponse>> validateTaxReceipts(
            @RequestBody List<TaxReceiptValidationRequest> requests
    ){
        log.info("[validateTaxReceipts] 컨트롤러 작동");
        return ResponseEntity.ok(new RestResponse<>(taxReceiptValidationService.getAdditionalAuthResponse(requests)));
    }

    @Override
    @PostMapping("/addition")
    public ResponseEntity<RestResponse<List<EasyCodefResponse>>> multipleAdditionAuthController(
            @RequestParam String transactionId
    ){
        log.info("[multipleAdditionAuthController] 컨트롤러 작동");
        return ResponseEntity.ok(
                new RestResponse<>(taxReceiptValidationService.getMultipleTaxReceiptValidationResponse(transactionId))
        );
    }
}
