package com.seoulmilk.receipt.infrastructure;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.context.properties.ConfigurationProperties;

@ConfigurationProperties(prefix = "codef.api")
@Getter
@RequiredArgsConstructor
public class OAuth2TokenProvider {
    private final String clientId;
    private final String clientSecret;
    private final String oAuth2Url;
    private final String taxReceiptUrl;
}
