package com.seoulmilk.core.util;

import com.seoulmilk.core.exception.DomainException;
import com.seoulmilk.core.util.dto.SendEmailRequest;
import com.seoulmilk.core.util.exception.MailErrorCode;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;

@ExtendWith(MockitoExtension.class)
public class EmailUtilTest {
    @InjectMocks
    private EmailUtil emailUtil;

    private SendEmailRequest sendEmailRequest;

    @DisplayName("SendEmailRequest의 to 값이 올바르지 않은 이메일 형식의 경우 예외를 반환한다.")
    @Test
    void throwExceptionWhenToInRequestIsInvalid() {
        // GIVEN
        sendEmailRequest = new SendEmailRequest("test", "Test Subject", "Test Content");

        // WHEN & THEN
        DomainException ex = assertThrows(DomainException.class, () -> emailUtil.send(sendEmailRequest), "올바르지 않은 이메일 주소입니다.");
        assertThat(ex.getCode()).isEqualTo(MailErrorCode.INVALID_MAIL_ADDRESS.name());
        assertThat(ex.getMessage()).isEqualTo(MailErrorCode.INVALID_MAIL_ADDRESS.getMessage());
    }

    @DisplayName("SendEmailRequest의 to 값이 null인 경우 예외를 반환한다.")
    @Test
    void throwExceptionWhenToInRequestIsNull() {
        // GIVEN
        sendEmailRequest = new SendEmailRequest(null, "Test Subject", "Test Content");

        // WHEN & THEN
        DomainException ex = assertThrows(DomainException.class, () -> emailUtil.send(sendEmailRequest), "올바르지 않은 이메일 주소입니다.");
        assertThat(ex.getCode()).isEqualTo(MailErrorCode.INVALID_MAIL_ADDRESS.name());
        assertThat(ex.getMessage()).isEqualTo(MailErrorCode.INVALID_MAIL_ADDRESS.getMessage());
    }

    @DisplayName("SendEmailRequest의 subject가 비어있는 경우 예외를 반환한다.")
    @Test
    void throwExceptionWhenSubjectInRequestIsNull() {
        // GIVEN
        sendEmailRequest = new SendEmailRequest("test@naver.com", "", "Test Content");

        // WHEN & THEN
        DomainException ex = assertThrows(DomainException.class, () -> emailUtil.send(sendEmailRequest), "메일 제목이 없습니다.");
        assertThat(ex.getCode()).isEqualTo(MailErrorCode.NO_MAIL_TITLE.name());
        assertThat(ex.getMessage()).isEqualTo(MailErrorCode.NO_MAIL_TITLE.getMessage());
    }

    @DisplayName("SendEmailRequest의 text가 비어있는 경우 예외를 반환한다.")
    @Test
    void throwExceptionWhenTextInRequestIsNull() {
        // GIVEN
        sendEmailRequest = new SendEmailRequest("test@naver.com", "Test Subject", "");

        // WHEN & THEN
        DomainException ex = assertThrows(DomainException.class, () -> emailUtil.send(sendEmailRequest), "메일 내용이 없습니다.");
        assertThat(ex.getCode()).isEqualTo(MailErrorCode.NO_MAIL_CONTENT.name());
        assertThat(ex.getMessage()).isEqualTo(MailErrorCode.NO_MAIL_CONTENT.getMessage());
    }
}
