package com.seoulmilk.emp.domain.value;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum HomeTax {
    // 1:카카오톡, 2:페이코, 3:삼성패스, 4:KB모바일, 5:통신사(PASS), 6:네이버, 7:신한인증서, 8: toss, 9: 뱅크샐러드
    KAKAO("1"),
    PAYCO("2"),
    SAMSUNG_PASS("3"),
    KB_MOBILE("4"),
    PASS("5"),
    NAVER("6"),
    SHINHAN("7"),
    TOSS("8"),
    BANK_SALAD("9");


    private final String value;
}
