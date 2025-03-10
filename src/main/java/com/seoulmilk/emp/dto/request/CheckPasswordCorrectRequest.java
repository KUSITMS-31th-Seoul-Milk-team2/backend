package com.seoulmilk.emp.dto.request;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;

public record CheckPasswordCorrectRequest(
        @NotNull(message = "비밀번호는 필수 입력 항목입니다.")
        @Schema(description = "비밀번호", example = "12341234")
        String password
) {
}
