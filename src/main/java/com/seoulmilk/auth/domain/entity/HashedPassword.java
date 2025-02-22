package com.seoulmilk.auth.domain.entity;

import lombok.Value;

@Value
public class HashedPassword {
    String value;

    private HashedPassword(String value) {
        this.value = value;
    }

    public static HashedPassword of(String value) {
        return new HashedPassword(value);
    }
}
