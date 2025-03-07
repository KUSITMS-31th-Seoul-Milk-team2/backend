package com.seoulmilk.receipt.dto.request;

import com.seoulmilk.notice.domain.entity.Notice;
import com.seoulmilk.notice.dto.response.PageNoticeResponse;
import com.seoulmilk.receipt.domain.entity.ValidReceipt;
import io.swagger.v3.oas.annotations.media.Schema;
import org.springframework.data.domain.Page;

import java.util.List;

public record PageValidReceiptResponse<T>(
        @Schema(description = "지급결의서 목록")
        List<T> content,

        @Schema(description = "페이지 번호", example = "0")
        int pageNo,

        @Schema(description = "페이지 크기", example = "10")
        int pageSize,

        @Schema(description = "전체 지급결의서 수", example = "100")
        long totalElements,

        @Schema(description = "전체 페이지 수", example = "10")
        int totalPages,

        @Schema(description = "마지막 페이지 여부", example = "false")
        boolean last
) {
    public static <T>PageValidReceiptResponse<T> create(List<T> content, Page<ValidReceipt> validReceipts){
        return new PageValidReceiptResponse<>(
                content,
                validReceipts.getNumber(),
                validReceipts.getSize(),
                validReceipts.getNumberOfElements(),
                validReceipts.getTotalPages(),
                validReceipts.isLast()
        );
    }
}
