package com.seoulmilk.notice.dto.request;

import io.swagger.v3.oas.annotations.media.Schema;

public record ReadNoticeRequest(
        @Schema(description = "공지사항 ID", example = "1")
        Long id
) {
}
