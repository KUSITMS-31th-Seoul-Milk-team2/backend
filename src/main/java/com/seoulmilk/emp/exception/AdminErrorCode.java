package com.seoulmilk.emp.exception;

import com.seoulmilk.core.exception.DomainException;
import com.seoulmilk.core.exception.error.BaseErrorCode;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;


@Getter
@RequiredArgsConstructor
public enum AdminErrorCode implements BaseErrorCode<DomainException> {
    NOT_ADMIN_EXCEPTION(HttpStatus.FORBIDDEN, "관리자 권한이 필요합니다."),
    ALREADY_ADMINISTRATOR(HttpStatus.BAD_REQUEST, "이미 관리자 권한을 가진 사원입니다."),
    EMP_NOT_FOUND(HttpStatus.NOT_FOUND, "일부 직원 PK가 존재하지 않습니다.");

    private final HttpStatus httpStatus;

    private final String message;

    @Override
    public DomainException toException() {
        return new DomainException(httpStatus, this);
    }
}

