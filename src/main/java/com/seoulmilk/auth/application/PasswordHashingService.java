package com.seoulmilk.auth.application;

import com.seoulmilk.auth.domain.entity.HashedPassword;
import com.seoulmilk.auth.domain.entity.Password;

public interface PasswordHashingService {
    HashedPassword hash(Password password);
    void matches(String rawPassword, HashedPassword hashedPassword);
}
