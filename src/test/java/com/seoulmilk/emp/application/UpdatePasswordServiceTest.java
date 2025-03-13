package com.seoulmilk.emp.application;

import com.seoulmilk.core.infrastructure.security.CustomUserDetails;
import com.seoulmilk.core.util.emailUtil.EmailUtil;
import com.seoulmilk.emp.domain.service.PasswordService;
import com.seoulmilk.emp.dto.request.UpdatePasswordRequest;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class UpdatePasswordServiceTest {
    @Mock
    PasswordService passwordService;

    @Mock
    EmailUtil emailUtil;

    @InjectMocks
    UpdatePasswordService updatePasswordService;

    private final CustomUserDetails userDetails = mock(CustomUserDetails.class);
    private final UpdatePasswordRequest updatePasswordRequest = new UpdatePasswordRequest("oldPassword", "newPassword");

    @Test
    @DisplayName("비밀번호 변경 성공")
    public void updatePassword_success() {
        // Given
        doNothing().when(passwordService).validatePassword(userDetails, updatePasswordRequest);

        // When
        updatePasswordService.updatePassword(userDetails, updatePasswordRequest);

        // Then
        verify(passwordService, times(1)).validatePassword(userDetails, updatePasswordRequest);
        verify(emailUtil, times(1)).send(any());
        verify(passwordService, times(1)).updatePassword(userDetails, updatePasswordRequest);
    }
}
