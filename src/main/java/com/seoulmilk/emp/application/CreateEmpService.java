package com.seoulmilk.emp.application;

import com.seoulmilk.auth.application.PasswordHashingService;
import com.seoulmilk.core.infrastructure.security.CustomUserDetails;
import com.seoulmilk.emp.domain.entity.Emp;
import com.seoulmilk.emp.domain.repository.EmpRepository;
import com.seoulmilk.emp.domain.value.*;
import com.seoulmilk.emp.dto.request.GrantPrivilegeRequest;
import com.seoulmilk.emp.dto.response.CreateEmpResponse;
import com.seoulmilk.emp.dto.response.GrantPrivilegeResponse;
import com.seoulmilk.emp.exception.AdminErrorCode;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Log4j2
public class CreateEmpService {
    private final EmpRepository empRepository;
    private final PasswordHashingService passwordHashingService;

    @Transactional
    public CreateEmpResponse create(CustomUserDetails customUserDetails, GrantPrivilegeRequest grantPrivilegeRequest) {
        if (customUserDetails.getRole() != Role.ADMIN) {
            log.error("[CreateEmpService.create] 관리자만 사원을 생성할 수 있습니다. 접근 유저 PK: {}", customUserDetails.getId());
            throw AdminErrorCode.NOT_ADMIN_EXCEPTION.toException();
        }

        Emp createdEmp = Emp.create(
                grantPrivilegeRequest.name(),
                grantPrivilegeRequest.employeeId(),
                "testEmail@test.com",
                Role.EMPLOYEE,
                "01012345678",
                Telecom.KT,
                "19900101",
                HashedPassword.of(passwordHashingService.hash(Password.from("12345678")).getValue()),
                HomeTax.KB_MOBILE
        );

        empRepository.save(createdEmp);
        empRepository.grantPrivilege(createdEmp);
        return CreateEmpResponse.of(createdEmp);
    }

}
