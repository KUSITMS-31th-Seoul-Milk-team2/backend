package com.seoulmilk.receipt.infrastructure;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.context.properties.ConfigurationProperties;

import java.util.Base64;

@ConfigurationProperties(prefix = "codef.api")
@Getter
@RequiredArgsConstructor
public class OAuth2TokenProvider {
    private final String clientId;
    private final String clientSecret;
    private final String oAuth2Url;
    private final String taxReceiptUrl;

    public String createAuthHeader(){
        String auth = clientId + ":" +clientSecret;
        String authEnc = Base64.getEncoder().encodeToString(auth.getBytes());
        return "Basic " + authEnc;
    }
}
