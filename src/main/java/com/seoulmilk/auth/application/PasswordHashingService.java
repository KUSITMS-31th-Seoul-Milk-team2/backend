package com.seoulmilk.auth.application;

import com.seoulmilk.emp.domain.entity.HashedPassword;
import com.seoulmilk.emp.domain.entity.Password;

public interface PasswordHashingService {
    HashedPassword hash(Password password);
    void matches(String rawPassword, HashedPassword hashedPassword);
}
