package com.seoulmilk.receipt.infrastructure.properties;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.context.properties.ConfigurationProperties;

@ConfigurationProperties(prefix = "codef.api")
@Getter
@RequiredArgsConstructor
public class EasyCodefProperties {
    private final String clientId;
    private final String clientSecret;
    private final String publicKey;
    private final String endpoint;
}

