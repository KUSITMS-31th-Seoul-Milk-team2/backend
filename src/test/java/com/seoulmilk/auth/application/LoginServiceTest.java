package com.seoulmilk.auth.application;

import com.seoulmilk.auth.domain.entity.HashedPassword;
import com.seoulmilk.auth.exception.AuthenticationErrorCode;
import com.seoulmilk.auth.presentation.dto.request.LoginRequest;
import com.seoulmilk.auth.presentation.dto.response.LoginResponse;
import com.seoulmilk.emp.domain.entity.Emp;
import com.seoulmilk.emp.domain.repository.EmpRepository;
import com.seoulmilk.emp.domain.value.Role;
import com.seoulmilk.emp.exception.EmpErrorCode;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.BDDMockito;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.assertj.core.api.AssertionsForClassTypes.assertThatThrownBy;

@ExtendWith(MockitoExtension.class)
public class LoginServiceTest {
    @Mock
    private EmpRepository empRepository;

    @Mock
    private PasswordHashingService passwordHashingService;

    @Mock
    private TokenProvider tokenProvider;

    @InjectMocks
    private LoginService loginService;

    private final String VALID_EMPLOYEE_ID = "12341234";
    private final String VALID_PASSWORD = "validPassword123!";

    @BeforeEach
    void resetMocks() {
        BDDMockito.reset(empRepository, passwordHashingService, tokenProvider);
    }

    @Test
    @DisplayName("유효한 사번과 비밀번호로 로그인 성공")
    void login_success() {
        // Given
        Emp mockEmp = createMockEmp();
        LoginRequest request = new LoginRequest(VALID_EMPLOYEE_ID, VALID_PASSWORD);
        String expectedToken = "mockAccessToken";

        BDDMockito.given(empRepository.findByEmployeeId(VALID_EMPLOYEE_ID))
                .willReturn(Optional.of(mockEmp));
        BDDMockito.willDoNothing().given(passwordHashingService)
                .matches(VALID_PASSWORD, mockEmp.getPassword());
        BDDMockito.given(tokenProvider.provideAccessToken(mockEmp))
                .willReturn(expectedToken);

        // When
        LoginResponse response = loginService.login(request);

        // Then
        assertThat(response).isNotNull();
        assertThat(response.accessToken()).isEqualTo(expectedToken);
        assertThat(response.userInfo().employeeId()).isEqualTo(VALID_EMPLOYEE_ID);

        BDDMockito.then(empRepository).should().findByEmployeeId(VALID_EMPLOYEE_ID);
        BDDMockito.then(passwordHashingService).should().matches(VALID_PASSWORD, HashedPassword.of(VALID_PASSWORD));
        BDDMockito.then(tokenProvider).should().provideAccessToken(mockEmp);
    }

    @Test
    @DisplayName("존재하지 않는 사번으로 로그인 시도 시 예외 발생")
    void login_fail_whenEmployeeNotExist() {
        // Given
        String INVALID_EMPLOYEE_ID = "wrongId";
        LoginRequest request = new LoginRequest(INVALID_EMPLOYEE_ID, VALID_PASSWORD);
        BDDMockito.given(empRepository.findByEmployeeId(INVALID_EMPLOYEE_ID))
                .willReturn(Optional.empty());

        // When & Then
        assertThatThrownBy(() -> loginService.login(request))
                .isInstanceOf(EmpErrorCode.NOT_EXIST_EMPLOYEE.toException().getClass())
                .hasMessageContaining("사원이 존재하지 않습니다.");

        BDDMockito.then(empRepository).should().findByEmployeeId(INVALID_EMPLOYEE_ID);
        BDDMockito.then(passwordHashingService).shouldHaveNoInteractions();
        BDDMockito.then(tokenProvider).shouldHaveNoInteractions();
    }

        @Test
    @DisplayName("잘못된 비밀번호 입력 시 예외 발생")
    void login_fail_whenPasswordMismatch() {
        // Given
        Emp mockEmp = createMockEmp();
        String INVALID_PASSWORD = "wrongPassword";
        LoginRequest request = new LoginRequest(VALID_EMPLOYEE_ID, INVALID_PASSWORD);

        BDDMockito.given(empRepository.findByEmployeeId(VALID_EMPLOYEE_ID))
                .willReturn(Optional.of(mockEmp));
        BDDMockito.willThrow(AuthenticationErrorCode.PASSWORD_NOT_MATCH.toException())
                .given(passwordHashingService)
                .matches(INVALID_PASSWORD, mockEmp.getPassword());

        // When & Then
        assertThatThrownBy(() -> loginService.login(request))
                .isInstanceOf(AuthenticationErrorCode.PASSWORD_NOT_MATCH.toException().getClass())
                .hasMessageContaining("비밀번호가 일치하지 않습니다.");

        BDDMockito.then(empRepository).should().findByEmployeeId(VALID_EMPLOYEE_ID);
        BDDMockito.then(passwordHashingService).should().matches(INVALID_PASSWORD, HashedPassword.of(VALID_PASSWORD));
        BDDMockito.then(tokenProvider).shouldHaveNoInteractions();
    }

    private Emp createMockEmp() {
        return Emp.builder()
                .employeeId(VALID_EMPLOYEE_ID)
                .password(HashedPassword.of("validPassword123!"))
                .name("홍길동")
                .email("test@test.com")
                .role(Role.ADMIN)
                .phoneNumber("010-1234-5678")
                .build();
    }

}
