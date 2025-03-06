package com.seoulmilk.notice.dto.request;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;

public record UpdateNoticeRequest(
        @NotNull(message = "공지사항 ID는 필수입니다.")
        @Schema(description = "공지사항 ID", example = "1")
        Long id,

        @Schema(description = "수정한 공지사항 제목", example = "수정한 공지사항 제목")
        String title,

        @Schema(description = "수정한 공지사항 내용", example = "수정한 공지사항 내용")
        String content
) {
}
