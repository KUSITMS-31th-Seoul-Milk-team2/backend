package com.seoulmilk.auth.presentation.dto.response;

import com.seoulmilk.emp.domain.entity.Emp;
import io.swagger.v3.oas.annotations.media.Schema;

public record LoginResponse(
        @Schema(description = "사용자 정보")
        UserInfo userInfo,

        @Schema(description = "액세스 토큰")
        String accessToken

) {
    public record UserInfo(
            @Schema(description = "사용자 ID", example = "1L")
            Long id,

            @Schema(description = "사용자 이름", example = "홍길동")
            String name
    ) {
        public static UserInfo from(Emp employee) {
            return new UserInfo(employee.getId(), employee.getName());
        }
    }

    public static LoginResponse of(Emp employee, String accessToken) {
        return new LoginResponse(UserInfo.from(employee), accessToken);
    }
}
