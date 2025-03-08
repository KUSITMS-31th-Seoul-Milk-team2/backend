package com.seoulmilk.emp.dto.request;

import io.swagger.v3.oas.annotations.media.Schema;

public record GrantPrivilegeRequest(
        @Schema(description = "이름", example = "홍길동")
        String name,

        @Schema(description = "사번", example = "12341234")
        String employeeId
) {
}
