package com.seoulmilk.auth.infrastructure;

import com.seoulmilk.auth.application.PasswordHashingService;
import com.seoulmilk.auth.domain.entity.HashedPassword;
import com.seoulmilk.auth.domain.entity.Password;
import com.seoulmilk.auth.exception.AuthenticationErrorCode;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class BcryptPasswordHashingService implements PasswordHashingService {
    private final PasswordEncoder passwordEncoder;

    @Override
    public HashedPassword hash(Password password) {
        return HashedPassword.of(passwordEncoder.encode(password.getValue()));
    }

    @Override
    public void matches(String rawPassword, HashedPassword hashedPassword) {
        if (!passwordEncoder.matches(rawPassword, hashedPassword.getValue())) {
            throw AuthenticationErrorCode.PASSWORD_NOT_MATCH.toException();
        }
    }
}
