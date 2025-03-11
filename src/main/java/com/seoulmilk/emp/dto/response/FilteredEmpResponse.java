package com.seoulmilk.emp.dto.response;

import com.seoulmilk.emp.domain.value.Role;

public record FilteredEmpResponse(
        Long id,
        String name,
        String employeeId,
        Role role
) {
}
