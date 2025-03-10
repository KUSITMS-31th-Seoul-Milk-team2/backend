package com.seoulmilk.core.configuration.kafka;

import lombok.extern.log4j.Log4j2;
import org.springframework.kafka.listener.KafkaListenerErrorHandler;
import org.springframework.kafka.listener.ListenerExecutionFailedException;
import org.springframework.messaging.Message;
import org.springframework.stereotype.Component;

@Component("noRetryErrorHandler")
@Log4j2
public class NoRetryErrorHandler implements KafkaListenerErrorHandler {
    @Override
    public Object handleError(Message<?> message, ListenerExecutionFailedException exception) {
        log.error("Kafka 리스너 에러 발생: {}, 메시지: {}", exception.getMessage(), message, exception);
        return null;
    }
}