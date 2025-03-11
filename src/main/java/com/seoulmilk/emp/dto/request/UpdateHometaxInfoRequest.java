package com.seoulmilk.emp.dto.request;

import io.swagger.v3.oas.annotations.media.Schema;

public record UpdateHometaxInfoRequest(
        @Schema(name = "홈택스 번호", example = "KAKAO : 1, PAYCO : 2, SAMSUMG_PASS : 3, KB_MOBILE : 4, PASS : 5, NAVER : 6, SHINHAN : 7, TOSS : 8, BANK_SALAD : 9")
        String homeTaxNum
) {
}
