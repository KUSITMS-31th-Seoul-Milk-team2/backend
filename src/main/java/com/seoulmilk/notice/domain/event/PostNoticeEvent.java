package com.seoulmilk.notice.domain.event;

import org.springframework.context.ApplicationEvent;

public class PostNoticeEvent extends ApplicationEvent {
    public PostNoticeEvent(Object source) {
        super(source);
    }
}
