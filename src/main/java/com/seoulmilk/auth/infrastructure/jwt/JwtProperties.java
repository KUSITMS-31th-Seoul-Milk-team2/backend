package com.seoulmilk.auth.infrastructure.jwt;

import jakarta.annotation.PostConstruct;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.context.properties.ConfigurationProperties;

@ConfigurationProperties(prefix = "jwt")
@Getter
@RequiredArgsConstructor
public class JwtProperties {
    private final String secretKey;
    private final String issuer;
    private final long accessExpiration;
    private final long refreshExpiration;

    private TokenConfig access;
    private TokenConfig refresh;

    @Getter
    public static class TokenConfig {
        private final long expiration;
        private final String prefix = "Bearer ";

        public TokenConfig(long expiration) {
            this.expiration = expiration;
        }
    }

    @PostConstruct
    public void init() {
        this.access = new TokenConfig(accessExpiration);
        this.refresh = new TokenConfig(refreshExpiration);
    }
}
