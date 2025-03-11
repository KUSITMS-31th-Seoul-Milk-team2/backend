package com.seoulmilk.auth.application;

import com.seoulmilk.emp.domain.value.HashedPassword;
import com.seoulmilk.emp.domain.value.Password;

public interface PasswordHashingService {
    HashedPassword hash(Password password);
    void matches(String rawPassword, HashedPassword hashedPassword);
    HashedPassword generateInitialPassword(String phoneNumber);
}
