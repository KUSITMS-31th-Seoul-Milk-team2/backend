package com.seoulmilk.core.exception.error;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;

@Getter
@RequiredArgsConstructor
public enum GlobalErrorCode implements BaseErrorCode<RuntimeException> {

    ALREADY_PROCESS_STARTED(HttpStatus.BAD_REQUEST, "이미 처리중인 요청입니다."),
    INTERNAL_SERVER_ERROR(HttpStatus.INTERNAL_SERVER_ERROR, "알 수 없는 내부 오류입니다."),
    INVALID_FILE(HttpStatus.BAD_REQUEST, "유효하지 않은 파일입니다."),
    FILE_UPLOAD_ERROR(HttpStatus.INTERNAL_SERVER_ERROR, "써드파티 저장소에 파일 업로드 중 오류가 발생했습니다.");

    private final HttpStatus httpStatus;

    private final String message;

    @Override
    public RuntimeException toException() {
        return new RuntimeException(message);
    }
}
