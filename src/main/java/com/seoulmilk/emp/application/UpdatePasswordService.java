package com.seoulmilk.emp.application;

import com.seoulmilk.core.infrastructure.security.CustomUserDetails;
import com.seoulmilk.core.util.emailUtil.EmailUtil;
import com.seoulmilk.core.util.emailUtil.dto.SendEmailRequest;
import com.seoulmilk.core.util.emailUtil.factory.SendEmailRequestFactory;
import com.seoulmilk.emp.domain.service.PasswordService;
import com.seoulmilk.emp.dto.request.UpdatePasswordRequest;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;


@Service
@Log4j2
@RequiredArgsConstructor
public class UpdatePasswordService {

    private final PasswordService passwordService;
    private final EmailUtil emailUtil;

    @Transactional
    public void updatePassword(CustomUserDetails customUserDetails, UpdatePasswordRequest updatePasswordRequest) {
        passwordService.validatePassword(customUserDetails, updatePasswordRequest);
        SendEmailRequest sendEmailRequest = SendEmailRequestFactory.create(
                customUserDetails.getEmail(),
                updatePasswordRequest.newPassword()
        );
        emailUtil.send(sendEmailRequest);
        passwordService.updatePassword(customUserDetails, updatePasswordRequest);
    }
}
