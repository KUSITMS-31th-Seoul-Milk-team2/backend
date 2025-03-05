package com.seoulmilk.core.util.dto;

public record SendEmailRequest(
        String receiver,
        String title,
        String content
) {
}
