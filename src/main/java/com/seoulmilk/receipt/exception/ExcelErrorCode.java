package com.seoulmilk.receipt.exception;

import com.seoulmilk.core.exception.DomainException;
import com.seoulmilk.core.exception.error.BaseErrorCode;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;

@Getter
@RequiredArgsConstructor
public enum ExcelErrorCode implements BaseErrorCode<DomainException> {
    FAIL_TO_DOWNLOAD(HttpStatus.NOT_FOUND, "엑셀 다운로드에 실패하였습니다.");

    private final HttpStatus httpStatus;
    private final String Message;

    @Override
    public DomainException toException() {
        return new DomainException(httpStatus, this);
    }
}
