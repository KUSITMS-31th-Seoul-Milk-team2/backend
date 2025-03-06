package com.seoulmilk.notice.dto.response;

import com.seoulmilk.notice.domain.entity.Notice;
import io.swagger.v3.oas.annotations.media.Schema;
import org.springframework.data.domain.Page;

import java.util.List;

public record PageNoticeResponse<T>(
        @Schema(description = "공지사항 목록")
        List<T> content,

        @Schema(description = "페이지 번호", example = "0")
        int pageNo,

        @Schema(description = "페이지 크기", example = "10")
        int pageSize,

        @Schema(description = "전체 공지사항 수", example = "100")
        long totalElements,

        @Schema(description = "전체 페이지 수", example = "10")
        int totalPages,

        @Schema(description = "마지막 페이지 여부", example = "false")
        boolean last
) {
    public static <T> PageNoticeResponse<T> create(List<T> content, Page<Notice> notices) {
        return new PageNoticeResponse<>(content,
                notices.getNumber(),
                notices.getSize(),
                notices.getTotalElements(),
                notices.getTotalPages(),
                notices.isLast());
    }
}
