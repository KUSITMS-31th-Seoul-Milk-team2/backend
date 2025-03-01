package com.seoulmilk.core.configuration.kafka;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.context.properties.ConfigurationProperties;

@ConfigurationProperties(prefix = "kafka")
@Getter
@RequiredArgsConstructor
public class KafkaProperties {
    private final String topic;
    private final String groupId;
}
