package com.seoulmilk.notice.dto.response;

import java.util.List;

public record ReadPaginatedResponse<T>(
        List<T> data,
        Cursor cursor,
        int count,
        String next
) {
    public record Cursor(
            Long after
    ) {
    }

    public static <T> ReadPaginatedResponse<T> create(List<T> data, Long after, int count, String next) {
        return new ReadPaginatedResponse<>(data, new Cursor(after), count, next);
    }
}
