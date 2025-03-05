package com.seoulmilk.auth.presentation.dto.response;

import com.seoulmilk.emp.domain.entity.Emp;
import com.seoulmilk.emp.domain.value.Role;
import io.swagger.v3.oas.annotations.media.Schema;

public record LoginResponse(
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

            @Schema(description = "사용자 권한", example = "ADMIN")
            Role role
    ) {
        public static UserInfo from(Emp employee) {
            return new UserInfo(employee.getEmployeeId(), employee.getName(), employee.getPhoneNumber(), employee.getEmail(), employee.getRole());
        }
    }

    public static LoginResponse of(Emp employee) {
        return new LoginResponse(UserInfo.from(employee));
    }
}
