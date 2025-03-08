package com.seoulmilk.emp.application;

import com.seoulmilk.core.infrastructure.security.CustomUserDetails;
import com.seoulmilk.emp.domain.entity.Emp;
import com.seoulmilk.emp.domain.repository.EmpRepository;
import com.seoulmilk.emp.domain.value.Role;
import com.seoulmilk.emp.dto.request.GrantPrivilegeRequest;
import com.seoulmilk.emp.dto.response.GrantPrivilegeResponse;
import com.seoulmilk.emp.exception.AdminErrorCode;
import com.seoulmilk.emp.exception.EmpErrorCode;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.assertj.core.api.AssertionsForClassTypes.assertThatThrownBy;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class GrantPrivilegeServiceTest {

    @Mock
    private EmpRepository empRepository;

    @Mock
    private CustomUserDetails adminUserDetails;

    @InjectMocks
    private GrantPrivilegeService grantPrivilegeService;

    private final String VALID_EMPLOYEE_ID = "12341234";
    private final String VALID_NAME = "홍길동";
    private final String INVALID_EMPLOYEE_ID = "12345678";

    @BeforeEach
    void setUp() {
        when(adminUserDetails.getRole()).thenReturn(Role.ADMIN);
    }

    @Test
    @DisplayName("사번과 이름이 일치할 경우 권한 부여에 성공한다.")
    void grantPrivilege_SuccessWhenCredentialsMatch() {
        // Given
        Emp mockEmp = Emp.builder()
                .name(VALID_NAME)
                .employeeId(VALID_EMPLOYEE_ID)
                .build();

        when(empRepository.findByEmployeeName(VALID_NAME)).thenReturn(java.util.Optional.of(mockEmp));
        when(empRepository.findByEmployeeId(VALID_EMPLOYEE_ID)).thenReturn(java.util.Optional.of(mockEmp));
        GrantPrivilegeRequest validRequest = new GrantPrivilegeRequest(VALID_NAME, VALID_EMPLOYEE_ID);

        // When
        GrantPrivilegeResponse response = grantPrivilegeService.grantPrivilege(adminUserDetails, validRequest);

        // Then
        verify(empRepository, times(1)).grantPrivilege(mockEmp);
        assertThat(response.message()).isEqualTo("사원 권한 할당에 성공했습니다.");
    }

    @Test
    @DisplayName("관리자 권한 없는 사용자가 요청 시 AdminErrorCode.NOT_ADMIN_EXCEPTION 예외가 발생한다.")
    void grantPrivilege_FailWhenNotAdmin() {
        // Given
        when(adminUserDetails.getRole()).thenReturn(Role.EMPLOYEE);
        GrantPrivilegeRequest validRequest = new GrantPrivilegeRequest(VALID_NAME, VALID_EMPLOYEE_ID);

        // When & Then
        assertThatThrownBy(() -> grantPrivilegeService.grantPrivilege(adminUserDetails, validRequest))
                .isInstanceOf(AdminErrorCode.NOT_ADMIN_EXCEPTION.toException().getClass());
    }

    @Test
    @DisplayName("요청 이름이 null인 경우 EmpErrorCode.NOT_EXIST_EMPLOYEE 예외가 발생한다.")
    void grantPrivilege_FailWhenNameIsNull() {
        // Given
        GrantPrivilegeRequest nullNameRequest = new GrantPrivilegeRequest(null, VALID_EMPLOYEE_ID);

        // When & Then
        assertThatThrownBy(() -> grantPrivilegeService.grantPrivilege(adminUserDetails, nullNameRequest))
                .isInstanceOf(EmpErrorCode.CAN_NOT_FIND_EMPLOYEE_WITH_NAME.toException().getClass());
    }

    @Test
    @DisplayName("요청 사번이 null인 경우 EmpErrorCode.NOT_EXIST_EMPLOYEE 예외가 발생한다.")
    void grantPrivilege_FailWhenEmployeeIdIsNull() {
        // Given
        GrantPrivilegeRequest nullIdRequest = new GrantPrivilegeRequest(VALID_NAME, null);

        // When & Then
        assertThatThrownBy(() -> grantPrivilegeService.grantPrivilege(adminUserDetails, nullIdRequest))
                .isInstanceOf(EmpErrorCode.CAN_NOT_FIND_EMPLOYEE_WITH_EMPLOYEE_ID.toException().getClass());
    }


    @Test
    @DisplayName("Emp에 해당하는 사원 정보와 입력으로 들어온 Request  EmpErrorCode.INVALID_NAME_AND_EMPLOYEE_ID 예외가 발생한다.")
    void grantPrivilege_FailWhenCredentialsMismatch() {
        // Given
        Emp mockEmp = Emp.builder()
                .name(VALID_NAME)
                .employeeId(VALID_EMPLOYEE_ID)
                .build();

        when(empRepository.findByEmployeeName(VALID_NAME)).thenReturn(java.util.Optional.of(mockEmp));
        GrantPrivilegeRequest invalidRequest = new GrantPrivilegeRequest(VALID_NAME, INVALID_EMPLOYEE_ID);

        // When & Then
        assertThatThrownBy(() -> grantPrivilegeService.grantPrivilege(adminUserDetails, invalidRequest))
                .isInstanceOf(EmpErrorCode.INVALID_NAME_AND_EMPLOYEE_ID.toException().getClass());

        verify(empRepository, never()).grantPrivilege(any());
    }

    @Test
    @DisplayName("관리자에 권한을 할당 하려면 AdminErrorCode.ALREADY_ADMINISTRATOR 예외가 발생한다.")
    void grantPrivilege_CanNotGrantAdminPrivilegeToAdministrator() {
        // Given
        Emp mockEmp = Emp.builder()
                .name(VALID_NAME)
                .employeeId(VALID_EMPLOYEE_ID)
                .role(Role.ADMIN)
                .build();

        when(empRepository.findByEmployeeName(VALID_NAME)).thenReturn(java.util.Optional.of(mockEmp));
        GrantPrivilegeRequest validRequest = new GrantPrivilegeRequest(VALID_NAME, VALID_EMPLOYEE_ID);

        // When & Then
        assertThatThrownBy(() -> grantPrivilegeService.grantPrivilege(adminUserDetails, validRequest))
                .isInstanceOf(AdminErrorCode.ALREADY_ADMINISTRATOR.toException().getClass());

        verify(empRepository, never()).grantPrivilege(any());
    }
}
