package com.seoulmilk.auth.application;

import com.seoulmilk.auth.domain.factory.TokenRequestFactory;
import com.seoulmilk.auth.presentation.dto.request.LoginRequest;
import com.seoulmilk.auth.presentation.dto.response.LoginResponse;
import com.seoulmilk.emp.domain.entity.Emp;
import com.seoulmilk.emp.domain.repository.EmpRepository;
import com.seoulmilk.emp.exception.EmpErrorCode;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class LoginService {

    private final PasswordHashingService passwordHashingService;
    private final EmpRepository empRepository;
    private final TokenService tokenService;

    public LoginResponse login(LoginRequest request) {
        Emp employee = empRepository.findByEmployeeId(request.employeeId())
                .orElseThrow(EmpErrorCode.NOT_EXIST_EMPLOYEE::toException);
        passwordHashingService.matches(request.password(), employee.getPassword());
        String accessToken = tokenService.provideAccessToken(TokenRequestFactory.create(request.employeeId()));
        return LoginResponse.of(
                accessToken,
                employee
        );
    }
}
