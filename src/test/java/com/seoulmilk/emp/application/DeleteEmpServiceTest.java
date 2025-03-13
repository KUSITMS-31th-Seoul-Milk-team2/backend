package com.seoulmilk.emp.application;

import com.seoulmilk.core.infrastructure.security.CustomUserDetails;
import com.seoulmilk.emp.domain.entity.Emp;
import com.seoulmilk.emp.domain.repository.EmpRepository;
import com.seoulmilk.emp.domain.value.Role;
import com.seoulmilk.emp.dto.request.DeleteEmpsRequest;
import com.seoulmilk.emp.dto.response.DeleteEmpResponse;
import com.seoulmilk.emp.exception.AdminErrorCode;
import com.seoulmilk.notice.domain.repository.NoticeRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.assertj.core.api.AssertionsForClassTypes.assertThatThrownBy;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class DeleteEmpServiceTest {
    @Mock
    private EmpRepository empRepository;

    @Mock
    private NoticeRepository noticeRepository;

    @InjectMocks
    private DeleteEmpService deleteEmpService;

    private final CustomUserDetails userDetails = mock(CustomUserDetails.class);
    private final DeleteEmpsRequest validRequest = new DeleteEmpsRequest(List.of(1L, 2L));
    private final DeleteEmpsRequest inValidRequest = new DeleteEmpsRequest(List.of(1L, 2L, 4L, 5L));

    private List<Emp> createTestEmps(Long... authorIds) {
        return Arrays.stream(authorIds)
                .map(id -> Emp.builder().id(id).build())
                .collect(Collectors.toList());
    }


    @Test
    @DisplayName("사원 삭제 성공")
    public void deleteEmp_success() {
        // Given
        List<Emp> emps = createTestEmps(1L, 2L);
        when(empRepository.findAllByIds(validRequest.ids())).thenReturn(emps);
        when(userDetails.getId()).thenReturn(3L);
        when(userDetails.getRole()).thenReturn(Role.ADMIN);

        // When
        DeleteEmpResponse deleteEmpResponse = deleteEmpService.delete(userDetails, validRequest);

        // Then
        verify(empRepository).deleteAll(emps);
        verify(noticeRepository).deleteAllNoticesByEmps(emps);
        assertThat(deleteEmpResponse.success()).isTrue();
        assertThat(deleteEmpResponse.message()).isEqualTo("회원 삭제 성공");
    }

    @Test
    @DisplayName("사원 삭제 실패 : 관리자가 아닌 경우")
    public void deleteEmp_failed_notAdmin() {
        // Given
        when(userDetails.getRole()).thenReturn(Role.EMPLOYEE);

        // When & Then
        assertThatThrownBy(() -> deleteEmpService.delete(userDetails, validRequest))
                .isInstanceOf(AdminErrorCode.NOT_ADMIN_EXCEPTION.toException().getClass())
                .hasMessageContaining("관리자 권한이 필요합니다.");
    }

    @Test
    @DisplayName("사원 삭제 실패 : 삭제할 수 없는 회원이 포함되어 있는 경우")
    public void deletEmp_failed_empNotFound() {
        // Given
        List<Emp> emps = createTestEmps(1L, 2L);
        when(empRepository.findAllByIds(inValidRequest.ids())).thenReturn(emps);
        when(userDetails.getId()).thenReturn(3L);
        when(userDetails.getRole()).thenReturn(Role.ADMIN);

        // When & Then
        assertThatThrownBy(() -> deleteEmpService.delete(userDetails, inValidRequest))
                .isInstanceOf(AdminErrorCode.EMP_NOT_FOUND.toException().getClass())
                .hasMessageContaining("일부 직원 PK가 존재하지 않습니다.");
    }
}
