package com.seoulmilk.emp.dto.request;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;

public record UpdatePasswordRequest(
        @NotNull(message = "예전 비밀번호는 필수 입력 항목입니다.")
        @Schema(description = "예전 비밀번호", example = "12341234")
        String oldPassword,

        @NotNull(message = "새로운 비밀번호는 필수 입력 항목입니다.")
        @Schema(description = "새로운 비밀번호", example = "43214321")
        String newPassword
) {
}
