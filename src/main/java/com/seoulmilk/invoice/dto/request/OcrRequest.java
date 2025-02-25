package com.seoulmilk.invoice.dto.request;

import java.util.List;

public record OcrRequest(
        String version,
        String requestId,
        long timestamp,
        String lang,
        List<ImageInfo> images
) {
    public record ImageInfo(
            String format,
            String name
    ) {
    }
}
