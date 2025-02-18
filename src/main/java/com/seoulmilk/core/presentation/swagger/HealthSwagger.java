package com.seoulmilk.core.presentation.swagger;

import com.seoulmilk.core.presentation.RestResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.ResponseEntity;

@Tag(name = "Health", description = "헬스체크")
public interface HealthSwagger {
    @Operation(
            summary = "헬스 체크 API",
            description = "현재 서비스가 띄어져 있는지 확인합니다.",
            operationId = "/health"
    )
    ResponseEntity<RestResponse<String>> health();
}
