package com.seoulmilk.core.configuration.kafka;

import org.springframework.kafka.listener.KafkaListenerErrorHandler;
import org.springframework.kafka.listener.ListenerExecutionFailedException;
import org.springframework.messaging.Message;
import org.springframework.stereotype.Component;

@Component("noRetryErrorHandler")
public class NoRetryErrorHandler implements KafkaListenerErrorHandler {
    @Override
    public Object handleError(Message<?> message, ListenerExecutionFailedException exception) {
        return null;
    }
}