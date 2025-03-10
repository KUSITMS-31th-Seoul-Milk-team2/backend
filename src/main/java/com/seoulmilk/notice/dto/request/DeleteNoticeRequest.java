package com.seoulmilk.notice.dto.request;

import io.swagger.v3.oas.annotations.media.Schema;

import java.util.List;

public record DeleteNoticeRequest(
        @Schema(description = "삭제할 공지사항 ID", example = "[1, 2, 3]")
        List<Long> ids
) {
}
