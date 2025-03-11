package com.seoulmilk.emp.dto.response;

public record DeleteEmpResponse(
        Boolean success,
        String message
) {
    public static DeleteEmpResponse of(Boolean success, String message) {
        return new DeleteEmpResponse(success, message);
    }
}
