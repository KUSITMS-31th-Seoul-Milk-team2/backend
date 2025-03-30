package com.seoulmilk.notice.dto.request;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ReadNoticePagePaginatedRequest {
    private Long key;

    private Long page;

    private Order order__createdAt;

    private Long take;

    public ReadNoticePagePaginatedRequest() {
        this.page = 1L;
        this.key = Long.MAX_VALUE;
        this.order__createdAt = Order.DESC;
        this.take = 10L;
    }
}
