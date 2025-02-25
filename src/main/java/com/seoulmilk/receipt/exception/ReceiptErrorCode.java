package com.seoulmilk.receipt.exception;

import com.seoulmilk.core.exception.DomainException;
import com.seoulmilk.core.exception.error.BaseErrorCode;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;

@Getter
@RequiredArgsConstructor
public enum ReceiptErrorCode implements BaseErrorCode<DomainException> {
    INVALID_FORMAT_ERROR(HttpStatus.NOT_FOUND, "텍스트의 형식이 일치하지 않습니다."),
    UNAUHORIZED_USER_ERROR(HttpStatus.UNAUTHORIZED, "권한이 없는 유저가 사용중입니다.");

    private final HttpStatus httpStatus;

    private final String message;

    @Override
    public DomainException toException() {
        return new DomainException(httpStatus, this);
    }
}
