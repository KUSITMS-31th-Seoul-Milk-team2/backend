package com.seoulmilk.notice.exception;

import com.seoulmilk.core.exception.DomainException;
import com.seoulmilk.core.exception.error.BaseErrorCode;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;

@Getter
@RequiredArgsConstructor
public enum NoticeErrorCode implements BaseErrorCode<DomainException> {
    NOT_EXISTS_NOTICE(HttpStatus.BAD_REQUEST, "존재하지 않는 공지사항입니다."),
    NOT_AN_AUTHOR(HttpStatus.BAD_REQUEST, "자신이 쓴 공지만 수정 또는 삭제가 가능합니다."),
    INVALID_SEARCH_KEYWORD(HttpStatus.BAD_REQUEST, "검색조건이 존재하지 않습니다."),
    POST_NOTICE_FAILED(HttpStatus.INTERNAL_SERVER_ERROR, "공지사항 등록에 실패하였습니다.");


    private final HttpStatus httpStatus;

    private final String message;

    @Override
    public DomainException toException() {
        return new DomainException(httpStatus, this);
    }

}
