package com.seoulmilk.receipt.presentation.swagger;

import com.seoulmilk.core.presentation.RestResponse;
import com.seoulmilk.receipt.dto.request.UpdateValidReceiptRequest;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestBody;


@Tag(name = "CRUD", description = "세금계산서 관련 crud")
public interface TaxReceiptUpdateSwagger {
    @Operation(
            summary = "세금계산서 정보 수정 API",
            description = "검증이 완료된 세금계산서의 정보를 수정합니다.",
            operationId = "/v1/receipt/update"
    )
    ResponseEntity<RestResponse<Boolean>> updateValidReceipt(
            @RequestBody UpdateValidReceiptRequest updateValidReceiptRequest
    );
}
