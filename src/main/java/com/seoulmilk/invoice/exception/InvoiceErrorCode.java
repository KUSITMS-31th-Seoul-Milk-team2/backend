package com.seoulmilk.invoice.exception;

import com.seoulmilk.core.exception.DomainException;
import com.seoulmilk.core.exception.error.BaseErrorCode;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;

@Getter
@RequiredArgsConstructor
public enum InvoiceErrorCode implements BaseErrorCode<DomainException> {
    FAILED_TO_CREATE_JSON(HttpStatus.INTERNAL_SERVER_ERROR, "JSON 생성 중 에러가 발생했습니다."),
    EMPTY_FILE(HttpStatus.BAD_REQUEST, "파일이 비어있습니다."),
    NOT_SUPPORTED_EXTENSION(HttpStatus.BAD_REQUEST, "지원하지 않는 확장자입니다. (JPEG, PNG, PDF만 허용)"),
    NOT_SUPPORTED_FILE(HttpStatus.BAD_REQUEST, "지원하지 않는 파일 형식입니다. (JPEG, PNG, PDF만 허용)");

    private final HttpStatus httpStatus;

    private final String message;

    @Override
    public DomainException toException() {
        return new DomainException(httpStatus, this);
    }
}
