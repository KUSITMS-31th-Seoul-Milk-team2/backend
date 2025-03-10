package com.seoulmilk.receipt.presentation.controller.crud;

import com.seoulmilk.core.presentation.RestResponse;
import com.seoulmilk.receipt.application.query.TaxReceiptUpdateService;
import com.seoulmilk.receipt.dto.request.UpdateValidReceiptRequest;
import com.seoulmilk.receipt.presentation.swagger.TaxReceiptUpdateSwagger;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/v1/receipt")
@RequiredArgsConstructor
@Log4j2
public class TaxReceiptUpdateController implements TaxReceiptUpdateSwagger {
    private final TaxReceiptUpdateService taxReceiptUpdateService;

    @Override
    @PutMapping("/update")
    public ResponseEntity<RestResponse<Boolean>> updateValidReceipt(
            @RequestBody UpdateValidReceiptRequest updateValidReceiptRequest
    ) {
        taxReceiptUpdateService.updateValidReceipt(updateValidReceiptRequest);
        return ResponseEntity.ok(new RestResponse<>(true));
    }
}
