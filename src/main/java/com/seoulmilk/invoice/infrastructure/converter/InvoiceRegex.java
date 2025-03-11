package com.seoulmilk.invoice.infrastructure.converter;

import lombok.Getter;

import java.util.regex.Pattern;

@Getter
public enum InvoiceRegex {
    NON_DIGIT("[^0-9]", "숫자가 아닌 문자 제거"),
    BUSINESS_REGISTER_NUMBER("^\\d{10,13}$", "사업자등록번호(10~13자리 숫자)"),
    HYPHEN("-", "하이픈 제거"),
    WHITESPACE("\\s+", "공백 문자 제거");

    private final String pattern;
    private final String description;
    private final Pattern compiledPattern;

    InvoiceRegex(String pattern, String description) {
        this.pattern = pattern;
        this.description = description;
        this.compiledPattern = Pattern.compile(pattern);
    }

    public String removeFrom(String input) {
        return input.replaceAll(pattern, "");
    }
}
