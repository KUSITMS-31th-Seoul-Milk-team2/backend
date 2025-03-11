package com.seoulmilk.emp.dto.response;

import com.seoulmilk.emp.domain.entity.Emp;
import com.seoulmilk.emp.domain.value.HomeTax;
import com.seoulmilk.emp.domain.value.Role;
import com.seoulmilk.emp.domain.value.Telecom;
import io.swagger.v3.oas.annotations.media.Schema;

public record ReadEmpResponse(
        @Schema(description = "사원 PK")
        Long id,

        @Schema(description = "사원 번호")
        String employeeId,

        @Schema(description = "사원 이름")
        String name,

        @Schema(description = "사원 이메일")
        String email,

        @Schema(description = "사원 직급")
        Role role,

        @Schema(description = "사원 전화번호")
        String phoneNumber,

        @Schema(description = "사원 통신사")
        Telecom telecom,

        @Schema(description = "사원 생일")
        String birthday,

        @Schema(description = "사원 홈택스 정보")
        HomeTax hometax,

        @Schema(description = "사원 권한 할당 여부")
        Boolean isSignedIn
) {
    public static ReadEmpResponse of(Emp emp) {
        return new ReadEmpResponse(
                emp.getId(),
                emp.getEmployeeId(),
                emp.getName(),
                emp.getEmail(),
                emp.getRole(),
                emp.getPhoneNumber(),
                emp.getTelecom(),
                emp.getBirthday(),
                emp.getHometax(),
                emp.getIsSignedIn()
        );
    }
}
