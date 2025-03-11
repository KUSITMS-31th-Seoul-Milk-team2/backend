package com.seoulmilk.emp.dto.request;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;

public record UpdateHometaxInfoRequest(
        @NotNull(message = "홈택스 번호를 입력해주세요.")
        @Min(value = 1, message = "홈택스 번호는 1부터 9 사이입니다.")
        @Max(value = 9, message = "홈택스 번호는 1부터 9 사이입니다.")
        @Schema(name = "홈택스 번호", example = "KAKAO : 1, PAYCO : 2, SAMSUMG_PASS : 3, KB_MOBILE : 4, PASS : 5, NAVER : 6, SHINHAN : 7, TOSS : 8, BANK_SALAD : 9")
        String homeTaxNum
) {
}
