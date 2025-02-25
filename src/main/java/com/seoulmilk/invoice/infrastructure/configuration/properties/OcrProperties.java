package com.seoulmilk.invoice.infrastructure.configuration.properties;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.context.properties.ConfigurationProperties;

@ConfigurationProperties(prefix = "clova.ocr")
@Getter
@RequiredArgsConstructor
public class OcrProperties {
    private final String version;
    private final String requestId;
    private final String lang;
    private final String defaultFormat;
}
