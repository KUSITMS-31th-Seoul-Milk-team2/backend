package com.seoulmilk.emp.application;

import com.seoulmilk.auth.application.PasswordHashingService;
import com.seoulmilk.core.infrastructure.security.CustomUserDetails;
import com.seoulmilk.emp.domain.entity.Emp;
import com.seoulmilk.emp.domain.repository.EmpRepository;
import com.seoulmilk.emp.domain.value.HashedPassword;
import com.seoulmilk.emp.domain.value.Password;
import com.seoulmilk.emp.domain.value.Role;
import com.seoulmilk.emp.dto.request.GrantPrivilegeRequest;
import com.seoulmilk.emp.dto.response.GrantPrivilegeResponse;
import com.seoulmilk.emp.exception.AdminErrorCode;
import com.seoulmilk.emp.exception.EmpErrorCode;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Log4j2
public class GrantPrivilegeService {
    private final EmpRepository empRepository;
    private final PasswordHashingService passwordHashingService;

    @Transactional
    public GrantPrivilegeResponse grantPrivilege(CustomUserDetails customUserDetails, GrantPrivilegeRequest grantPrivilegeRequest) {
        if (customUserDetails.getRole() != Role.ADMIN) {
            log.error("[EmpowerEmpService.validateEmpowerEmpRequest] 관리자만 권한을 부여할 수 있습니다. role: {}", customUserDetails.getRole());
            throw AdminErrorCode.NOT_ADMIN_EXCEPTION.toException();
        }

        Emp emp = empRepository.findByEmployeeName(grantPrivilegeRequest.name()).orElseThrow(EmpErrorCode.CAN_NOT_FIND_EMPLOYEE_WITH_NAME::toException);

        validateEmp(grantPrivilegeRequest, emp);
        empRepository.grantPrivilege(emp, passwordHashingService.generateInitialPassword(emp.getPhoneNumber()));
        return GrantPrivilegeResponse.of("사원 권한 할당에 성공했습니다.", emp);
    }

    private void validateEmp(GrantPrivilegeRequest grantPrivilegeRequest, Emp emp) {
        isInvalidRequest(grantPrivilegeRequest);
        checkEmpExists(grantPrivilegeRequest, emp);
        checkEmpIsAdministrator(emp);
    }

    private void isInvalidRequest(GrantPrivilegeRequest grantPrivilegeRequest) {
        if (grantPrivilegeRequest.name() == null || grantPrivilegeRequest.employeeId() == null) {
            log.error("[EmpowerEmpService.validateEmpowerEmpRequest] 이름 또는 사번이 null입니다. name: {}, employeeId: {}", grantPrivilegeRequest.name(), grantPrivilegeRequest.employeeId());
            throw EmpErrorCode.INVALID_NAME_AND_EMPLOYEE_ID.toException();
        }
    }

    private void checkEmpExists(GrantPrivilegeRequest grantPrivilegeRequest, Emp emp) {
        if (empRepository.findByEmployeeId(grantPrivilegeRequest.employeeId()).isEmpty()) {
            log.error("[EmpowerEmpService.validateEmpowerEmpRequest] EmployeeId에 해당하는 사원이 존재하지 않습니다. employeeId: {}", grantPrivilegeRequest.employeeId());
            throw EmpErrorCode.CAN_NOT_FIND_EMPLOYEE_WITH_EMPLOYEE_ID.toException();
        }

        if (!emp.getEmployeeId().equals(grantPrivilegeRequest.employeeId())) {
            log.error("[EmpowerEmpService.validateEmpowerEmpRequest] 이름과 사번이 일치하지 않습니다. name: {}, employeeId: {}", grantPrivilegeRequest.name(), grantPrivilegeRequest.employeeId());
            throw EmpErrorCode.INVALID_NAME_AND_EMPLOYEE_ID.toException();
        }
    }

    private void checkEmpIsAdministrator(Emp emp) {
        if (emp.isAdministrator()) {
            log.error("[EmpowerEmpService.validateEmpowerEmpRequest] 이미 관리자 권한을 가진 사원입니다. name: {}, employeeId: {}", emp.getName(), emp.getEmployeeId());
            throw AdminErrorCode.ALREADY_ADMINISTRATOR.toException();
        }
    }
}
