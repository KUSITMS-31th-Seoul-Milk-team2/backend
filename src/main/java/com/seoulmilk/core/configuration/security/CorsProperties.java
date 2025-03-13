package com.seoulmilk.core.configuration.security;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.context.properties.ConfigurationProperties;

import java.util.List;

@ConfigurationProperties(prefix = "cors.allowed")
@Getter
@RequiredArgsConstructor
public class CorsProperties {
    private final String paths;
    private final List<String> origins;
    private final List<String> methods;
    private final List<String> headers;
    private final List<String> exposedHeaders;
    private final Long maxAge;
    private final Boolean credentials;
}
