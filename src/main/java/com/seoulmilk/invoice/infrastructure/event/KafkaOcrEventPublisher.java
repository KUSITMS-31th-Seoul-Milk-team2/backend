package com.seoulmilk.invoice.infrastructure.event;

import com.seoulmilk.core.configuration.kafka.KafkaProperties;
import com.seoulmilk.invoice.application.OcrEventPublisher;
import lombok.RequiredArgsConstructor;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class KafkaOcrEventPublisher implements OcrEventPublisher {
    private final KafkaTemplate<String, Object> kafkaTemplate;
    private final KafkaProperties kafkaProperties;

    @Override
    public void publish(Object event) {
        kafkaTemplate.send(kafkaProperties.getTopic(), event);
    }
}
