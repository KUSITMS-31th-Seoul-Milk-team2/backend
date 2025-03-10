package com.seoulmilk.receipt.presentation.swagger;

import com.seoulmilk.core.presentation.RestResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;

import java.util.List;

@Tag(name = "CRUD", description = "세금계산서 관련 crud")
public interface TaxReceiptDeleteSwagger {
    @Operation(
            summary = "단일 지급결의서 삭제 컨트롤러",
            description = "검증이 완료된 세금계산서에 대한 삭제를 수행합니다. " +
                    "단 하나의 pk값을 사용하여 하나의 영수증을 삭제합니다",
            operationId = "/v1/receipt/{pk}"
    )
    ResponseEntity<RestResponse<Boolean>> deleteReceiptById(@PathVariable Long pk);

    @Operation(
            summary = "다수 지급결의서 일괄 삭제 컨트롤러",
            description = "검증이 되지 않은 세금 계산서를 삭제합니다" +
                    "다수의 pk값을 사용하여 한번에 삭제합니다",
            operationId = "/v1/receipt/delete"
    )
    ResponseEntity<RestResponse<Boolean>> deleteReceiptByIds(@RequestBody List<Long> pkList);
}
