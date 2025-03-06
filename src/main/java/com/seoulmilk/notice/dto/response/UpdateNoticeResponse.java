package com.seoulmilk.notice.dto.response;

import io.swagger.v3.oas.annotations.media.Schema;

public record UpdateNoticeResponse(
        @Schema(description = "수정 성공 여부", example = "1(true)")
        int isUpdated
) {
}
