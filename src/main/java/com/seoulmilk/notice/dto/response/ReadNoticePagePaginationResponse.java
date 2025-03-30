package com.seoulmilk.notice.dto.response;


import java.util.List;

public record ReadNoticePagePaginationResponse<T>(
        List<T> data,
        int total
) {
    public static <T> ReadNoticePagePaginationResponse<T> create(List<T> data, int total) {
        return new ReadNoticePagePaginationResponse<>(data, total);
    }
}
