package com.seoulmilk.invoice.application.exception;

import com.seoulmilk.core.exception.DomainException;
import com.seoulmilk.core.exception.error.BaseErrorCode;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;

@Getter
@RequiredArgsConstructor
public enum EventErrorCode implements BaseErrorCode<DomainException> {
    FAILED_TO_PUBLISH_EVENT(HttpStatus.INTERNAL_SERVER_ERROR, "이벤트 발행 중 에러가 발생했습니다.");
    private final HttpStatus httpStatus;

    private final String message;

    @Override
    public DomainException toException() {
        return new DomainException(httpStatus, this);
    }
}
