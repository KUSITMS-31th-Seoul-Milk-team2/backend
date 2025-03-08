package com.seoulmilk.emp.dto.response;

import com.seoulmilk.emp.domain.entity.Emp;
import io.swagger.v3.oas.annotations.media.Schema;

public record GrantPrivilegeResponse(
        @Schema(description = "권한 할당 메시지", example = "사원 권한 할당에 성공했습니다.")
        String message,
        @Schema(description = "사원 정보")
        EmpInfo empInfo
) {
    public record EmpInfo(
            @Schema(description = "사원 PK", example = "1")
            Long empPk,

            @Schema(description = "사번", example = "12341234")
            String employeeId,

            @Schema(description = "사원 이름", example = "홍길동")
            String name
    ) {
    }

    public static GrantPrivilegeResponse of(String message, Emp emp) {
        return new GrantPrivilegeResponse(message, new EmpInfo(emp.getId(), emp.getEmployeeId(), emp.getName()));
    }
}
