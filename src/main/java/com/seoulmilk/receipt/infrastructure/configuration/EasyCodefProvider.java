package com.seoulmilk.receipt.infrastructure.configuration;

import com.seoulmilk.receipt.infrastructure.properties.EasyCodefProperties;
import io.codef.api.EasyCodef;
import io.codef.api.EasyCodefBuilder;
import io.codef.api.constants.CodefClientType;
import lombok.Getter;
import org.springframework.stereotype.Component;

@Component
@Getter
public class EasyCodefProvider {
    private final EasyCodef easyCodef;

    public EasyCodefProvider(EasyCodefProperties easyCodefProperties) {
        this.easyCodef = EasyCodefBuilder.builder()
                .clientType(CodefClientType.DEMO)
                .clientId(easyCodefProperties.getClientId())
                .clientSecret(easyCodefProperties.getClientSecret())
                .publicKey(easyCodefProperties.getPublicKey())
                .build();
    }
}
