package com.seoulmilk.emp.dto.response;

import com.seoulmilk.emp.domain.entity.Emp;
import io.swagger.v3.oas.annotations.media.Schema;

public record CreateEmpResponse(
        @Schema(
                name = "employeeId",
                example = "1234567890",
                description = "사원번호"
        )
        String employeeId,

        @Schema(
                name = "name",
                example = "홍길동",
                description = "이름"
        )
        String name
) {
    public static CreateEmpResponse of(Emp emp) {
        return new CreateEmpResponse(emp.getEmployeeId(), emp.getName());
    }

}
