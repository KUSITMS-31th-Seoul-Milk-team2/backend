package com.seoulmilk.notice.dto.response;

import com.seoulmilk.notice.domain.entity.Notice;
import io.swagger.v3.oas.annotations.media.Schema;
import org.springframework.data.domain.Page;

import java.io.Serializable;
import java.util.List;

public record PageResponse<T>(
        @Schema(description = "콘텐츠")
        List<T> content,

        @Schema(description = "페이지 번호", example = "0")
        int pageNo,

        @Schema(description = "마지막 페이지 여부", example = "false")
        boolean isLast
) implements Serializable {
    public static <T> PageResponse<T> create(List<T> content, Page<Notice> notices) {
        return new PageResponse<>(
                content,
                notices.getNumber(),
                notices.isLast()
        );
    }
}
