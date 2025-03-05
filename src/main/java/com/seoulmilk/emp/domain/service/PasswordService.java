package com.seoulmilk.emp.domain.service;

import com.seoulmilk.core.infrastructure.security.CustomUserDetails;
import com.seoulmilk.emp.domain.repository.EmpRepository;
import com.seoulmilk.emp.dto.reqeust.UpdatePasswordRequest;
import com.seoulmilk.emp.exception.EmpErrorCode;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Objects;

@Service
@Log4j2
@RequiredArgsConstructor
public class PasswordService {

    private final PasswordEncoder passwordEncoder;
    private final EmpRepository empRepository;

    public void validatePassword(CustomUserDetails customUserDetails, UpdatePasswordRequest updatePasswordRequest) {
        if (!passwordEncoder.matches(updatePasswordRequest.oldPassword(), customUserDetails.getPassword())) {
            log.error("이전 비밀번호와 일치하지 않습니다.");
            throw EmpErrorCode.WRONG_PASSWORD_ERROR.toException();
        }
        if (Objects.equals(updatePasswordRequest.oldPassword(), updatePasswordRequest.newPassword())) {
            log.error("이전 비밀번호와 새 비밀번호가 동일합니다.");
            throw EmpErrorCode.SAME_PASSWORD_ERROR.toException();
        }
    }

    public void updatePassword(CustomUserDetails customUserDetails, UpdatePasswordRequest updatePasswordRequest) {
        empRepository.updatePassword(customUserDetails.emp().getId(), passwordEncoder.encode(updatePasswordRequest.newPassword()));
    }
}
