package com.seoulmilk.notice.domain.event;

import lombok.RequiredArgsConstructor;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class PostNoticeEventPublisher {
    private final ApplicationEventPublisher applicationEventPublisher;

    public void publishPostNoticeEvent() {
        applicationEventPublisher.publishEvent(new PostNoticeEvent(this));
    }
}
