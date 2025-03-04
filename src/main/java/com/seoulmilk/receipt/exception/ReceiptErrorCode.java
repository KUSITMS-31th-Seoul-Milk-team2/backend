package com.seoulmilk.receipt.exception;

import com.seoulmilk.core.exception.DomainException;
import com.seoulmilk.core.exception.error.BaseErrorCode;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;

@Getter
@RequiredArgsConstructor
public enum ReceiptErrorCode implements BaseErrorCode<DomainException> {
    FAILED_TO_SAVE_RECEIPT(HttpStatus.INTERNAL_SERVER_ERROR, "세금 계산서 정보를 저장하는데 실패하였습니다."),
    NOT_EXIST_RECEIPT(HttpStatus.NOT_FOUND, "계산서 정보를 찾는데 실패하였습니다.");

    private final HttpStatus httpStatus;
    private final String Message;

    @Override
    public DomainException toException() {
        return new DomainException(httpStatus, this);
    }
}
