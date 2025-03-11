package com.seoulmilk.emp.dto.response;

import io.swagger.v3.oas.annotations.media.Schema;

import java.util.List;

public record ReadEmpsResponse(
        @Schema(description = "사원 목록")
        List<FilteredEmpResponse> emps
) {
    public static ReadEmpsResponse of(List<FilteredEmpResponse> emps) {
        return new ReadEmpsResponse(emps);
    }
}
