package com.seoulmilk.auth.presentation.dto.response;

import com.seoulmilk.emp.domain.entity.Emp;
import com.seoulmilk.emp.domain.value.Role;
import io.swagger.v3.oas.annotations.media.Schema;

import java.time.LocalDateTime;

public record LoginResponse(
        @Schema(description = "Access Token")
        String accessToken,

        @Schema(description = "사용자 정보")
        UserInfo userInfo
) {
    public record UserInfo(
            @Schema(description = "사원번호", example = "12341234")
            String employeeId,

            @Schema(description = "사용자 이름", example = "홍길동")
            String name,

            @Schema(description = "전화번호", example = "010-1234-1234")
            String phoneNumber,

            @Schema(description = "이메일", example = "test@test.com")
            String email,

            @Schema(description = "생년월일", example = "1990-01-01")
            String birthday,

            @Schema(description = "사용자 권한", example = "ADMIN")
            Role role,

            @Schema(description = "회원가입 날짜 및 시간", example = "2021-01-01T00:00:00")
            LocalDateTime createdAt
    ) {
        public static UserInfo from(Emp employee) {
            return new UserInfo(employee.getEmployeeId(), employee.getName(), employee.getPhoneNumber(), employee.getEmail(), employee.getBirthday(), employee.getRole(), employee.getCreatedAt());
        }
    }

    public static LoginResponse of(String accessToken, Emp employee) {
        return new LoginResponse(accessToken, UserInfo.from(employee));
    }
}
