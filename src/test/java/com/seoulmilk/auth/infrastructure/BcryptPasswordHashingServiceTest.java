package com.seoulmilk.auth.infrastructure;

import com.seoulmilk.auth.domain.entity.HashedPassword;
import com.seoulmilk.auth.domain.entity.Password;
import com.seoulmilk.auth.exception.AuthenticationErrorCode;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.crypto.password.PasswordEncoder;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class BcryptPasswordHashingServiceTest {
    @Mock
    private PasswordEncoder passwordEncoder;

    @InjectMocks
    private BcryptPasswordHashingService passwordHashingService;

    @Test
    @DisplayName("비밀번호 해싱 성공")
    void hash_password_success() {
        // Given
        Password password = Password.from("validPassword123!");
        HashedPassword hashedPassword = HashedPassword.of("hashedPasswordValue");
        when(passwordEncoder.encode(anyString())).thenReturn(hashedPassword.getValue());

        // When
        HashedPassword result = passwordHashingService.hash(password);

        // Then
        assertEquals(hashedPassword, result);
        verify(passwordEncoder, times(1)).encode(password.getValue());
    }

    @Test
    @DisplayName("비밀번호 일치 확인 성공")
    void matches_password_success() {
        // Given
        HashedPassword hashedPassword = HashedPassword.of("hashedPasswordValue");
        when(passwordEncoder.matches(anyString(), anyString())).thenReturn(true);

        // When
        passwordHashingService.matches("rawPassword", hashedPassword);

        // Then
        verify(passwordEncoder, times(1)).matches("rawPassword", hashedPassword.getValue());
    }

    @Test
    @DisplayName("비밀번호 일치 확인 실패")
    void matches_password_fail() {
        // Given
        HashedPassword hashedPassword = HashedPassword.of("hashedPasswordValue");
        when(passwordEncoder.matches(anyString(), anyString())).thenReturn(false);

        // When & Then
        assertThrows(AuthenticationErrorCode.PASSWORD_NOT_MATCH.toException().getClass(), () -> {
            passwordHashingService.matches("rawPassword", hashedPassword);
        });
        verify(passwordEncoder, times(1)).matches("rawPassword", hashedPassword.getValue());
    }
}
