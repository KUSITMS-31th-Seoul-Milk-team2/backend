package com.seoulmilk.invoice.dto.response;

import java.util.List;

public record OcrResponse(
        String version,
        String requestId,
        long timestamp,
        List<ImageResult> images
) {
    public record ImageResult(
            List<Field> fields,
            Title title
    ) {
        public record Field(
                String name,
                String inferText
        ) {
        }

        public record Title(
                String name,
                String inferText
        ) {
        }
    }
}
