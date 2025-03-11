package com.seoulmilk.emp.dto.request;

import io.swagger.v3.oas.annotations.media.Schema;

import java.util.List;

public record DeleteEmpsRequest(
        @Schema(description = "삭제 할 사원 ID 리스트", example = "[1, 2, 3]")
        List<Long> ids
) {
}
