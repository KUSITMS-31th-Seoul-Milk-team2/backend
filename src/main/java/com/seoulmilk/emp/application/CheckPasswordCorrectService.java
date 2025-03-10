package com.seoulmilk.emp.application;

import com.seoulmilk.core.infrastructure.security.CustomUserDetails;
import com.seoulmilk.emp.domain.service.PasswordService;
import com.seoulmilk.emp.dto.request.CheckPasswordCorrectRequest;
import com.seoulmilk.emp.dto.response.CheckPasswordCorrectResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class CheckPasswordCorrectService {
    private final PasswordService passwordService;

    public CheckPasswordCorrectResponse checkPasswordCorrect(CustomUserDetails customUserDetails, CheckPasswordCorrectRequest checkPasswordCorrectRequest) {
        passwordService.validatePassword(customUserDetails, checkPasswordCorrectRequest);
        return CheckPasswordCorrectResponse.of("비밀번호가 일치합니다.");
    }
}
