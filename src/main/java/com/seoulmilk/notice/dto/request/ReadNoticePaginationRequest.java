package com.seoulmilk.notice.dto.request;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ReadNoticePaginationRequest {
    private Long key;

    private Order order__createdAt;

    private Long take;

    public ReadNoticePaginationRequest() {
        this.key = Long.MAX_VALUE;
        this.order__createdAt = Order.DESC;
        this.take = 10L;
    }
}
