package com.seoulmilk.notice.dto.request;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;

public record PostNoticeRequest(
        @NotNull(message = "제목은 필수 입력 항목입니다.")
        @Schema(description = "제목", example = "공지사항 제목")
        String title,

        @NotNull(message = "내용은 필수 입력 항목입니다.")
        @Schema(description = "내용", example = "공지사항 내용")
        String content
) {
}
