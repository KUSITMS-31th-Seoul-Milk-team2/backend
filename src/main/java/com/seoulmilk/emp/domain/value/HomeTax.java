package com.seoulmilk.emp.domain.value;

import com.seoulmilk.core.exception.error.GlobalErrorCode;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum HomeTax {
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

    public static HomeTax fromValue(String value) {
        for (HomeTax homeTax : values()) {
            if (homeTax.value.equals(value)) {
                return homeTax;
            }
        }
        throw GlobalErrorCode.NOT_EXIST_HOMETAX.toException();
    }

}
