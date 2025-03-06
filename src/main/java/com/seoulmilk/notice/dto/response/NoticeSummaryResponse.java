package com.seoulmilk.notice.dto.response;

import com.seoulmilk.emp.domain.entity.Emp;
import com.seoulmilk.notice.domain.entity.Notice;
import io.swagger.v3.oas.annotations.media.Schema;

public record NoticeSummaryResponse(
        @Schema(description = "공지사항 번호", example = "1")
        Long id,

        @Schema(description = "공지사항 제목", example = "공지사항 제목")
        String title,

        @Schema(description = "공지사항 작성자 이름", example = "홍길동")
        String author,

        @Schema(description = "공지사항 작성일", example = "2021-01-01 00:00:00")
        String createdAt
) {

    public static NoticeSummaryResponse create(Notice notice, Emp author) {
        return new NoticeSummaryResponse(
                notice.getId(),
                notice.getTitle(),
                author.getName(),
                notice.getCreatedAt().toString()
        );
    }

}
