package com.seoulmilk.auth.application;

import com.seoulmilk.auth.dto.request.TokenRequest;
import com.seoulmilk.emp.domain.entity.Emp;
import com.seoulmilk.emp.domain.repository.EmpRepository;
import com.seoulmilk.emp.exception.EmpErrorCode;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static org.assertj.core.api.AssertionsForClassTypes.assertThatThrownBy;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class TokenServiceTest {
    @Mock
    private TokenProvider tokenProvider;

    @Mock
    private EmpRepository empRepository;

    @InjectMocks
    private TokenService tokenService;

    TokenRequest tokenRequest = mock(TokenRequest.class);

    @Test
    @DisplayName("토큰 발급 테스트 성공")
    void provideAccessToken_success() {
        // given
        Optional<Emp> emp = Optional.of(Emp.builder()
                .employeeId("12341234")
                .build());
        when(tokenRequest.employeeId()).thenReturn("12341234");
        when(empRepository.findByEmployeeId("12341234")).thenReturn(emp);

        // when
        tokenService.provideAccessToken(tokenRequest);
        // then
        verify(tokenProvider, times(1)).provideAccessToken(emp.get());
    }

    @Test
    @DisplayName("토큰 발급 테스트 실패 : 존재하지 않는 직원")
    void provideAccessToken_fail_notExistEmp() {
        // given
        when(tokenRequest.employeeId()).thenReturn("12341234");
        when(empRepository.findByEmployeeId("12341234")).thenReturn(Optional.empty());

        // when & then
        assertThatThrownBy(() -> tokenService.provideAccessToken(tokenRequest))
                .isInstanceOf(EmpErrorCode.NOT_EXIST_EMPLOYEE.toException().getClass())
                .hasMessageContaining("사원이 존재하지 않습니다.");
    }

}
