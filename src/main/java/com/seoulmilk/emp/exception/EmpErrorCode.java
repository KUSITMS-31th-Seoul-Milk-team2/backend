package com.seoulmilk.emp.exception;

import com.seoulmilk.core.exception.DomainException;
import com.seoulmilk.core.exception.error.BaseErrorCode;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;

@Getter
@RequiredArgsConstructor
public enum EmpErrorCode implements BaseErrorCode<DomainException> {
    FAILED_TO_SAVE_EMPLOYEE(HttpStatus.INTERNAL_SERVER_ERROR, "사원 정보를 저장하는데 실패하였습니다."),
    NOT_EXIST_EMPLOYEE(HttpStatus.NOT_FOUND, "사원이 존재하지 않습니다.");

    private final HttpStatus httpStatus;

    private final String message;

    @Override
    public DomainException toException() {
        return new DomainException(httpStatus, this);
    }
}
