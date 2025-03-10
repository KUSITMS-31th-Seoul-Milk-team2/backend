package com.seoulmilk.receipt.presentation;

import com.seoulmilk.core.presentation.RestResponse;
import com.seoulmilk.receipt.application.query.TaxReceiptSearchService;
import com.seoulmilk.receipt.domain.entity.ValidReceipt;
import com.seoulmilk.receipt.dto.request.ValidResponseSearchRequest;
import com.seoulmilk.receipt.presentation.swagger.TaxReceiptSearchSwagger;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/v1/receipt")
@RequiredArgsConstructor
@Log4j2
public class TaxReceiptSearchController implements TaxReceiptSearchSwagger {
    private final TaxReceiptSearchService taxReceiptSearchService;

    @Override
    @PostMapping("/search")
    public ResponseEntity<RestResponse<List<ValidReceipt>>> getValidReceipts(
            @RequestBody ValidResponseSearchRequest request
    ) {
        List<ValidReceipt> validReceipts =
                taxReceiptSearchService.findAllValidReceiptPage(request);

        return ResponseEntity.ok(
                new RestResponse<>(validReceipts)
        );
    }
}
