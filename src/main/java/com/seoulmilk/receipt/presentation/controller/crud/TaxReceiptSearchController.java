package com.seoulmilk.receipt.presentation.controller.crud;

import com.seoulmilk.core.infrastructure.security.CustomUserDetails;
import com.seoulmilk.core.presentation.RestResponse;
import com.seoulmilk.emp.domain.value.Role;
import com.seoulmilk.receipt.application.query.TaxReceiptSearchService;
import com.seoulmilk.receipt.domain.entity.ValidReceipt;
import com.seoulmilk.receipt.dto.request.ValidResponseSearchRequest;
import com.seoulmilk.receipt.infrastructure.persistence.jpa.entity.InValidReceiptJpaEntity;
import com.seoulmilk.receipt.presentation.dto.response.TaxReceiptDataResponse;
import com.seoulmilk.receipt.presentation.swagger.TaxReceiptSearchSwagger;
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
public class TaxReceiptSearchController implements TaxReceiptSearchSwagger {
    private final TaxReceiptSearchService taxReceiptSearchService;

    @Override
    @PostMapping("/search")
    public ResponseEntity<RestResponse<List<ValidReceipt>>> getValidReceipts(
            @AuthenticationPrincipal CustomUserDetails customUserDetails,
            @RequestBody ValidResponseSearchRequest request
    ) {
        List<ValidReceipt> validReceipts = taxReceiptSearchService.findAllValidReceipt(
                customUserDetails,
                request
        );

        return ResponseEntity.ok(
                new RestResponse<>(validReceipts)
        );
    }

    @Override
    @GetMapping("/invalid/search")
    public ResponseEntity<RestResponse<List<InValidReceiptJpaEntity>>> getInvalidReceipts(CustomUserDetails customUserDetails) {
        return ResponseEntity.ok(
                new RestResponse<>(
                        taxReceiptSearchService.findByUserId(customUserDetails.getEmployeeId())
                )
        );
    }

    @Override
    @GetMapping("/valid/search-one")
    public ResponseEntity<RestResponse<TaxReceiptDataResponse>> getInvalidReceipts(
            @RequestParam Long id
    ) {
        return ResponseEntity.ok(new RestResponse<>(taxReceiptSearchService.findByReceiptId(id)));
    }
}
