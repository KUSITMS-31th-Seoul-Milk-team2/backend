package com.seoulmilk.emp.application;

import com.seoulmilk.core.infrastructure.security.CustomUserDetails;
import com.seoulmilk.emp.domain.entity.Emp;
import com.seoulmilk.emp.domain.repository.EmpRepository;
import com.seoulmilk.emp.domain.value.Role;
import com.seoulmilk.emp.exception.AdminErrorCode;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static org.assertj.core.api.AssertionsForClassTypes.assertThatThrownBy;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class ReadEmpsServiceTest {

    @Mock
    private EmpRepository empRepository;

    @InjectMocks
    public ReadEmpsService readEmpsService;

    private final CustomUserDetails userDetails = mock(CustomUserDetails.class);

    @Test
    @DisplayName("사원 목록 조회 성공")
    public void readEmps_success() {
        // Given
        when(userDetails.getRole()).thenReturn(Role.ADMIN);

        // When
        readEmpsService.read(userDetails);

        // Then
        verify(empRepository, times(1)).findAllWithNeededInfo();
    }


    @Test
    @DisplayName("사원 목록 조회 실패 : 관리자가 아닌 경우")
    public void readEmps_fail_not_admin() {
        // Given
        when(userDetails.getRole()).thenReturn(Role.EMPLOYEE);

        // When, Then
        assertThatThrownBy(() -> readEmpsService.read(userDetails))
                .isInstanceOf(AdminErrorCode.NOT_ADMIN_EXCEPTION.toException().getClass())
                .hasMessageContaining("관리자 권한이 필요합니다.");
        verify(empRepository, never()).findAllWithNeededInfo();
    }

    @Test
    @DisplayName("사원 이름 조회 성공")
    public void readEmpByName_success() {
        // Given
        when(userDetails.getRole()).thenReturn(Role.ADMIN);
        String name = "홍길동";
        Emp emp = Emp.builder().name(name).build();
        when(empRepository.findByEmployeeName(name)).thenReturn(Optional.of(emp));

        // When
        readEmpsService.readEmpByName(userDetails, name);

        // Then
        verify(empRepository, times(1)).findByEmployeeName(name);
    }

    @Test
    @DisplayName("사원 이름 조회 실패 : 관리자가 아닌 경우")
    public void readEmpByName_fail_not_admin() {
        // Given
        when(userDetails.getRole()).thenReturn(Role.EMPLOYEE);
        String name = "홍길동";

        // When, Then
        assertThatThrownBy(() -> readEmpsService.readEmpByName(userDetails, name))
                .isInstanceOf(AdminErrorCode.NOT_ADMIN_EXCEPTION.toException().getClass())
                .hasMessageContaining("관리자 권한이 필요합니다.");
        verify(empRepository, never()).findByEmployeeName(name);
    }

}
