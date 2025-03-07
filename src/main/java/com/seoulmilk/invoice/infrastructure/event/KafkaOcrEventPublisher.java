package com.seoulmilk.invoice.infrastructure.event;

import com.seoulmilk.core.configuration.kafka.KafkaProperties;
import com.seoulmilk.invoice.application.OcrEventPublisher;
import com.seoulmilk.receipt.dto.request.OcrValidationRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
@RequiredArgsConstructor
public class KafkaOcrEventPublisher implements OcrEventPublisher {
    private final KafkaTemplate<String, List<OcrValidationRequest>> kafkaTemplate;
    private final KafkaProperties kafkaProperties;

    @Override
    public void publish(List<OcrValidationRequest> event) {
        kafkaTemplate.send(kafkaProperties.getTopic(), event);
    }
}
