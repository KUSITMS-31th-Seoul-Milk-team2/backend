package com.seoulmilk.auth.domain.entity;

import lombok.Value;

@Value
public class Password {
    String value;

    private Password(String value) {
        this.value = value;
    }

    public static Password from(String value) {
        return new Password(value);
    }
}
