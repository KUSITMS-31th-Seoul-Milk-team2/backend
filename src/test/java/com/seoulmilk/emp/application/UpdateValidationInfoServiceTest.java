package com.seoulmilk.emp.application;

import com.seoulmilk.auth.exception.HometaxErrorCode;
import com.seoulmilk.core.exception.error.GlobalErrorCode;
import com.seoulmilk.core.infrastructure.security.CustomUserDetails;
import com.seoulmilk.emp.domain.repository.EmpRepository;
import com.seoulmilk.emp.domain.value.HomeTax;
import com.seoulmilk.emp.dto.request.UpdateHometaxInfoRequest;
import com.seoulmilk.emp.dto.response.UpdateHometaxInfoResponse;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class UpdateValidationInfoServiceTest {
    @Mock
    private EmpRepository empRepository;

    @InjectMocks
    private UpdateValidationInfoService updateValidationInfoService;

    private final CustomUserDetails userDetails = mock(CustomUserDetails.class);
    private final UpdateHometaxInfoRequest updateHometaxInfoRequest = mock(UpdateHometaxInfoRequest.class);

    @Test
    @DisplayName("홈택스 정보 업데이트 성공")
    public void updateHometaxInfo_success() {
        // Given
        when(userDetails.getId()).thenReturn(1L);
        when(userDetails.getHomeTax()).thenReturn(HomeTax.fromValue("1"));
        when(updateHometaxInfoRequest.homeTaxNum()).thenReturn("2");

        // When
        UpdateHometaxInfoResponse updateHometaxInfoResponse = updateValidationInfoService.updateHometaxInfo(userDetails, updateHometaxInfoRequest);;

        // Then
        verify(empRepository, times(1)).updateHometaxInfo(1L, HomeTax.fromValue("2"));
        assertThat(updateHometaxInfoResponse.isSuccess()).isTrue();
        assertThat(updateHometaxInfoResponse.message()).isEqualTo("홈택스 정보가 성공적으로 업데이트 되었습니다.");
    }

    @Test
    @DisplayName("홈택스 정보 업데이트 실패 : 동일한 홈택스 정보")
    public void updateHometaxInfo_failed_sameHometax() {
        // given
        when(userDetails.getId()).thenReturn(1L);
        when(userDetails.getHomeTax()).thenReturn(HomeTax.fromValue("1"));
        when(updateHometaxInfoRequest.homeTaxNum()).thenReturn("1");

        // when & then
        assertThatThrownBy(() -> updateValidationInfoService.updateHometaxInfo(userDetails, updateHometaxInfoRequest))
                .isInstanceOf(HometaxErrorCode.SAME_HOMETAX.toException().getClass())
                .hasMessageContaining("동일한 홈택스로 수정할 수 없습니다.");
        verify(empRepository, never()).updateHometaxInfo(1L, HomeTax.fromValue("1"));
    }

    @Test
    @DisplayName("홈택스 정보 업데이트 실패 : 존재하지 않는 홈택스")
    public void updateHometaxInfo_failed_notExistHometax() {
        // given
        when(userDetails.getId()).thenReturn(1L);
        when(userDetails.getHomeTax()).thenReturn(HomeTax.fromValue("1"));
        when(updateHometaxInfoRequest.homeTaxNum()).thenReturn("10");

        // when & then
        assertThatThrownBy(() -> updateValidationInfoService.updateHometaxInfo(userDetails, updateHometaxInfoRequest))
                .isInstanceOf(GlobalErrorCode.NOT_EXIST_HOMETAX.toException().getClass())
                .hasMessageContaining("존재하지 않는 홈택스입니다.");
    }

}
