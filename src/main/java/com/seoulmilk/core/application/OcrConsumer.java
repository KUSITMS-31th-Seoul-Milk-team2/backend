package com.seoulmilk.core.application;

import com.seoulmilk.receipt.dto.request.OcrValidationRequest;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;

@Service
@Log4j2
@RequiredArgsConstructor
public class OcrConsumer {

    @KafkaListener(
            topics = "${kafka.topic}",
            groupId = "${kafka.group-id}"
    )
    public void consume(OcrValidationRequest ocrValidationRequest) {
        log.info("OCR 결과: {}", ocrValidationRequest);
    }
}
