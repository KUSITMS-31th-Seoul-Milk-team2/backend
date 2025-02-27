package com.seoulmilk.receipt.exception;

import com.seoulmilk.core.exception.DomainException;
import com.seoulmilk.core.exception.error.BaseErrorCode;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;

@Getter
@RequiredArgsConstructor
public enum ReceiptErrorCode implements BaseErrorCode<DomainException> {
    INVALID_FORMAT_ERROR(HttpStatus.BAD_REQUEST, "텍스트의 형식이 일치하지 않습니다."),
    UNAUTHORIZED_USER_ERROR(HttpStatus.FORBIDDEN, "권한이 없는 유저가 사용중입니다."),
    OAUTH2_TOKEN_ERROR(HttpStatus.UNAUTHORIZED, "oAuth2 토큰 에러입니다."),
    JSON_DESERIALIZED_ERROR(HttpStatus.INTERNAL_SERVER_ERROR,
            "JSON 역직렬화 에러입니다. 응답 포맷이 맞지 않습니다.");

    private final HttpStatus httpStatus;

    private final String message;

    @Override
    public DomainException toException() {
        return new DomainException(httpStatus, this);
    }
}
