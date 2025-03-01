package com.seoulmilk.core.application;

import com.seoulmilk.auth.application.PasswordHashingService;
import com.seoulmilk.auth.domain.entity.HashedPassword;
import com.seoulmilk.auth.domain.entity.Password;
import com.seoulmilk.emp.domain.entity.Emp;
import com.seoulmilk.emp.domain.repository.EmpRepository;
import com.seoulmilk.emp.domain.value.Role;
import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class EmpInitializationService {
    private final EmpRepository empRepository;
    private final PasswordHashingService passwordHashingService;

    @PostConstruct
    public void init() {

        for (int i = 0; i < 5; i++) {
            empRepository.save(Emp.create(
                    "관리자" + i,
                    "12341234" + i,
                    "test" + i + "@test.com",
                    Role.ADMIN,
                    "010-1234-1234",
                    HashedPassword.of(passwordHashingService.hash(Password.from("12341234")).getValue())
            ));
        }

        for (int i = 0; i < 10; i++) {
            empRepository.save(Emp.create(
                    "사원" + i,
                    "43214321" + i,
                    "test" + i + "@test.com",
                    Role.EMPLOYEE,
                    "010-1234-1234",
                    HashedPassword.of(passwordHashingService.hash(Password.from("12341234")).getValue())
            ));
        }
    }
}
