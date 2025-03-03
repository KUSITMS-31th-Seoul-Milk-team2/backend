package com.seoulmilk.receipt.exception;

import com.seoulmilk.core.exception.DomainException;
import com.seoulmilk.core.exception.error.BaseErrorCode;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;

@Getter
@RequiredArgsConstructor
public enum ReceiptErrorCode implements BaseErrorCode<DomainException> {
    ERROR_TO_CONNECT_CODEF_SERVER(HttpStatus.INTERNAL_SERVER_ERROR, "CODEF 서버와의 연결이 이뤄지지 않았습니다"),
    ADDITIONAL_AUTHENTICATION_ERROR(HttpStatus.NON_AUTHORITATIVE_INFORMATION, "추가인증 정보를 전달하지 못했습니다."),
    ERROR_TO_GET_DATA(HttpStatus.INTERNAL_SERVER_ERROR, "응답 정보를 받는 것을 실패하였습니다."),
    INVALID_FORMAT_ERROR(HttpStatus.BAD_REQUEST, "잘못된 형식으로 요청값이 전달되었습니다.");

    private final HttpStatus httpStatus;

    private final String message;

    @Override
    public DomainException toException() {
        return new DomainException(httpStatus, this);
    }
}
