package com.seoulmilk.receipt.infrastructure.properties;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

@Component
@ConfigurationProperties(prefix = "codef.api")
@Setter
@Getter
public class EasyCodefProperties {
    private String clientId;
    private String clientSecret;
    private String publicKey;
}

