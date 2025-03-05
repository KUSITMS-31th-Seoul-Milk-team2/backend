package com.seoulmilk.invoice.infrastructure.converter;

public final class InvoiceRegexPatterns {
    public static final String NON_DIGIT = "[^0-9]";

    public static final String BUSINESS_REGISTER_NUMBER = "^[0-9]{10,13}$";

    public static final String HYPHEN = "-";
    public static final String WHITESPACE = "\\s+";

    private InvoiceRegexPatterns() {}
}
