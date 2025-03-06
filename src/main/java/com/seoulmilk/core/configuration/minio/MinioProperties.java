package com.seoulmilk.core.configuration.minio;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.context.properties.ConfigurationProperties;

@ConfigurationProperties(prefix = "minio")
@Getter
@RequiredArgsConstructor
public class MinioProperties {
    private final String endpoint;
    private final String accessKey;
    private final String secretKey;
    private final String bucket;
}
