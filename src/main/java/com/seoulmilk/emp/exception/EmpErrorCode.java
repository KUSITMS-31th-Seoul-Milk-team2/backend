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
    NOT_EXIST_EMPLOYEE(HttpStatus.NOT_FOUND, "사원이 존재하지 않습니다."),
    WRONG_PASSWORD_ERROR(HttpStatus.BAD_REQUEST, "비밀번호가 일치하지 않습니다."),
    SAME_PASSWORD_ERROR(HttpStatus.BAD_REQUEST, "기존 비밀번호와 새 비밀번호가 동일합니다.");

    private final HttpStatus httpStatus;

    private final String message;

    @Override
    public DomainException toException() {
        return new DomainException(httpStatus, this);
    }
}
