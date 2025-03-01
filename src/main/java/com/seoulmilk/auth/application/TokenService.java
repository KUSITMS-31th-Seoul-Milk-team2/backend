package com.seoulmilk.auth.application;

import com.seoulmilk.auth.dto.request.TokenRequest;
import com.seoulmilk.emp.domain.entity.Emp;
import com.seoulmilk.emp.domain.repository.EmpRepository;
import com.seoulmilk.emp.exception.EmpErrorCode;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class TokenService {
    private final TokenProvider tokenProvider;
    private final EmpRepository empRepository;

    public String provideAccessToken(TokenRequest request) {
        Emp emp = empRepository.findByEmployeeId(request.employeeId())
                .orElseThrow(EmpErrorCode.NOT_EXIST_EMPLOYEE::toException);
        return tokenProvider.provideAccessToken(emp);
    }
}
