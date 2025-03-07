package com.seoulmilk.receipt.presentation;

import com.seoulmilk.core.presentation.RestResponse;
import com.seoulmilk.notice.dto.response.PageNoticeResponse;
import com.seoulmilk.receipt.application.TaxReceiptSearchService;
import com.seoulmilk.receipt.domain.entity.ValidReceipt;
import com.seoulmilk.receipt.dto.request.PageValidReceiptResponse;
import com.seoulmilk.receipt.dto.request.ValidResponseSearchRequest;
import com.seoulmilk.receipt.presentation.swagger.TaxReceiptSearchSwagger;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springdoc.core.annotations.ParameterObject;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/v1/receipt/search")
@RequiredArgsConstructor
@Log4j2
public class TaxReceiptSearchController implements TaxReceiptSearchSwagger {
    private final TaxReceiptSearchService taxReceiptSearchService;

    @Override
    @PostMapping
    public ResponseEntity<RestResponse<PageValidReceiptResponse<ValidReceipt>>> getValidReceiptsByPage(
            @RequestBody ValidResponseSearchRequest request,
            @ParameterObject
            @PageableDefault(size = 10, sort = "id", direction = Sort.Direction.DESC) Pageable pageable
    ) {
        PageValidReceiptResponse<ValidReceipt> pageValidReceiptResponse =
                taxReceiptSearchService.findAllValidReceiptPage(request, pageable);

        return ResponseEntity.ok(
                new RestResponse<>(pageValidReceiptResponse)
        );
    }
}
