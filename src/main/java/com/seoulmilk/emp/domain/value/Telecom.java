package com.seoulmilk.emp.domain.value;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum Telecom {
    SKT("0"),
    KT("1"),
    LG("2");

    private final String telecomNum;
}
