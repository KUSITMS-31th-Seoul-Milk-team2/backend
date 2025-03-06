package com.seoulmilk.core.util.emailUtil.dto;

public record SendEmailRequest(
        String receiver,
        String title,
        String content
) {
}
