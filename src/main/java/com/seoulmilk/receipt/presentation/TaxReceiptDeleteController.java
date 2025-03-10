package com.seoulmilk.receipt.presentation;

import com.seoulmilk.core.presentation.RestResponse;
import com.seoulmilk.receipt.application.query.TaxReceiptDeleteService;
import com.seoulmilk.receipt.presentation.swagger.TaxReceiptDeleteSwagger;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/v1/receipt")
@RequiredArgsConstructor
@Log4j2
public class TaxReceiptDeleteController implements TaxReceiptDeleteSwagger {
    private final TaxReceiptDeleteService taxReceiptDeleteService;

    @Override
    @DeleteMapping("/{pk}")
    public ResponseEntity<RestResponse<Boolean>> deleteReceiptById(
            @PathVariable Long pk
    ) {
        taxReceiptDeleteService.deleteValidReceipt(pk);
        return ResponseEntity.ok(new RestResponse<>(true));
    }

    @Override
    @DeleteMapping("/delete")
    public ResponseEntity<RestResponse<Boolean>> deleteReceiptByIds(
            @RequestBody List<Long> pkList
    ) {
        taxReceiptDeleteService.deleteInValidReceipts(pkList);
        return ResponseEntity.ok(new RestResponse<>(true));
    }
}
