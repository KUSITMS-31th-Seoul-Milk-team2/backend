package com.seoulmilk.notice.dto.response;

import com.seoulmilk.emp.domain.entity.Emp;
import com.seoulmilk.notice.domain.entity.Notice;
import io.swagger.v3.oas.annotations.media.Schema;

public record ReadNoticeResponse(
        @Schema(description = "공지사항 ID", example = "1")
        Long id,

        @Schema(description = "제목", example = "공지사항 제목")
        String title,

        @Schema(description = "작성자", example = "홍길동")
        String author,

        @Schema(description = "내용", example = "공지사항 내용")
        String content,

        @Schema(description = "첨부 파일 URL", example = "https://seoulmilk.com/notice/1")
        String fileUrl,

        @Schema(description = "작성일", example = "2021-01-01T00:00:00")
        String createdAt
) {
    public static ReadNoticeResponse create(Notice notice, Emp author) {
        return new ReadNoticeResponse(
                notice.getId(),
                notice.getTitle(),
                author.getName(),
                notice.getContent(),
                notice.getFileUrl(),
                notice.getCreatedAt().toString()
        );
    }
}
