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

            @Schema(description = "사용자 권한", example = "ADMIN")
            Role role
    ) {
        public static UserInfo from(Emp employee) {
            return new UserInfo(employee.getEmployeeId(), employee.getName(), employee.getRole());
        }
    }

    public static LoginResponse of(Emp employee) {
        return new LoginResponse(UserInfo.from(employee));
    }
}
