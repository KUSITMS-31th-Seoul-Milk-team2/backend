package com.seoulmilk.emp.application;

import com.seoulmilk.core.infrastructure.security.CustomUserDetails;
import com.seoulmilk.emp.domain.service.PasswordService;
import com.seoulmilk.emp.dto.request.CheckPasswordCorrectRequest;
import com.seoulmilk.emp.dto.response.CheckPasswordCorrectResponse;
import com.seoulmilk.emp.exception.EmpErrorCode;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.AssertionsForClassTypes.assertThatThrownBy;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class CheckPasswordCorrectServiceTest {
    @Mock
    private PasswordService passwordService;

    @InjectMocks
    private CheckPasswordCorrectService checkPasswordCorrectService;

    @Test
    @DisplayName("비밀번호가 일치하는 경우 성공 응답을 반환한다.")
    void checkPasswordCorrect_ValidPassword_ReturnsSuccessResponse() {
        // given
        CustomUserDetails customUserDetails = mock(CustomUserDetails.class);
        CheckPasswordCorrectRequest request = mock(CheckPasswordCorrectRequest.class);

        // when
        doNothing().when(passwordService).validatePassword(customUserDetails, request);
        CheckPasswordCorrectResponse response = checkPasswordCorrectService.checkPasswordCorrect(customUserDetails, request);

        // then
        verify(passwordService, times(1)).validatePassword(customUserDetails, request);
        assertThat(response).isNotNull();
        assertThat(response.message()).isEqualTo("비밀번호가 일치합니다.");
    }

    @Test
    @DisplayName("비밀번호가 일치하지 않는 경우 EmpErrorCode.WRONG_PASSWORD_ERROR를 반환한다.")
    void checkPasswordCorrect_InvalidPassword_ThrowsWrongPasswordError() {
        // given
        CustomUserDetails customUserDetails = mock(CustomUserDetails.class);
        CheckPasswordCorrectRequest request = mock(CheckPasswordCorrectRequest.class);

        // when
        doThrow(EmpErrorCode.WRONG_PASSWORD_ERROR.toException()).when(passwordService).validatePassword(customUserDetails, request);

        // when, then
        assertThatThrownBy(() -> checkPasswordCorrectService.checkPasswordCorrect(customUserDetails, request))
                .isInstanceOf(EmpErrorCode.WRONG_PASSWORD_ERROR.toException().getClass());
    }
}
