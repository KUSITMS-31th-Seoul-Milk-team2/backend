package com.seoulmilk.auth.presentation.dto.request;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;

public record LoginRequest(
        @NotNull(message = "사번은 필수 입력 항목입니다.")
        @Schema(description = "사번", example = "12341234")
        String employeeId,

        @NotNull(message = "비밀번호는 필수 입력 항목입니다.")
        @Schema(description = "비밀번호", example = "password")
        String password
) {
}
