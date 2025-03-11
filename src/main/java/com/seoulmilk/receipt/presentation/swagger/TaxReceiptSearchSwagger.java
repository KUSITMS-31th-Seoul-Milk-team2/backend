package com.seoulmilk.receipt.presentation.swagger;

import com.seoulmilk.core.infrastructure.security.CustomUserDetails;
import com.seoulmilk.core.presentation.RestResponse;
import com.seoulmilk.receipt.domain.entity.ValidReceipt;
import com.seoulmilk.receipt.dto.request.ValidResponseSearchRequest;
import com.seoulmilk.receipt.infrastructure.persistence.jpa.entity.InValidReceiptJpaEntity;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;

@Tag(name = "CRUD", description = "세금계산서 관련 crud")
public interface TaxReceiptSearchSwagger {
    @Operation(
            summary = "세금계산서 키워드 검색 API",
            description = "키워드를 통해 지급결의서를 조회합니다. 키워드가 없는경우 전체 지급 결의서를 조회합니다.",
            operationId = "/v1/receipt/search"
    )
    ResponseEntity<RestResponse<List<ValidReceipt>>> getValidReceipts(
            @AuthenticationPrincipal CustomUserDetails customUserDetails,
            @RequestBody ValidResponseSearchRequest request
    );


    @Operation(
            summary = "본인이 만든 불일치 세금계산서 전체 조회 API",
            description = "본인의 사번을 통해 본인이 요청한 영수증 중 불일치 세금계산서 정보를 불러옵니다.",
            operationId = "/v1/receipt/invalid/search"
    )
    ResponseEntity<RestResponse<List<InValidReceiptJpaEntity>>> getInvalidReceipts(
            @AuthenticationPrincipal CustomUserDetails customUserDetails
    );
}
