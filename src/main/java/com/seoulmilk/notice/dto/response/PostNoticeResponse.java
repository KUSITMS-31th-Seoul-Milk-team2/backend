package com.seoulmilk.notice.dto.response;

import com.seoulmilk.notice.domain.entity.Notice;
import io.swagger.v3.oas.annotations.media.Schema;

public record PostNoticeResponse(
        @Schema(description = "공지 ID", example = "1")
        Long id,

        @Schema(description = "제목", example = "공지사항 제목")
        String title,

        @Schema(description = "내용", example = "공지사항 내용")
        String content,

        @Schema(description = "파일 URL", example = "https://seoulmilk.com/notice/1 (null 가능)")
        String fileUrl
) {
    public static PostNoticeResponse create(Notice notice) {
        return new PostNoticeResponse(notice.getId(), notice.getTitle(), notice.getContent(), notice.getFileUrl());
    }
}
