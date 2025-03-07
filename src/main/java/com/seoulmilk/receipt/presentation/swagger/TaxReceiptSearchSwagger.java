package com.seoulmilk.receipt.presentation.swagger;

import com.seoulmilk.core.presentation.RestResponse;
import com.seoulmilk.notice.dto.response.PageNoticeResponse;
import com.seoulmilk.receipt.domain.entity.ValidReceipt;
import com.seoulmilk.receipt.dto.request.PageValidReceiptResponse;
import com.seoulmilk.receipt.dto.request.ValidResponseSearchRequest;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.hibernate.query.Page;
import org.springdoc.core.annotations.ParameterObject;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestBody;

@Tag(name = "Search", description = "세금계산서 검색 api")
public interface TaxReceiptSearchSwagger {
    @Operation(
            summary = "세금계산서 키워드 검색 API",
            description = "키워드를 통해 지급결의서를 조회합니다. 키워드가 없는경우 전체 지급 결의서를 조회합니다.",
            operationId = "/v1/search/list"
    )
    ResponseEntity<RestResponse<PageValidReceiptResponse<ValidReceipt>>> getValidReceiptsByPage(
            @RequestBody ValidResponseSearchRequest request,
            @ParameterObject
            @PageableDefault(size = 10, sort = "id", direction = Sort.Direction.DESC) Pageable pageable
    );

}
