package com.seoulmilk.emp.application;

import com.seoulmilk.core.infrastructure.security.CustomUserDetails;
import com.seoulmilk.emp.domain.repository.EmpRepository;
import com.seoulmilk.emp.domain.value.HomeTax;
import com.seoulmilk.emp.dto.request.UpdateHometaxInfoRequest;
import com.seoulmilk.emp.dto.response.UpdateHometaxInfoResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class UpdateValidationInfoService {
    private final EmpRepository empRepository;

    @Transactional
    public UpdateHometaxInfoResponse updateHometaxInfo(
            CustomUserDetails customUserDetails,
            UpdateHometaxInfoRequest updateHometaxInfoRequest
    ) {
        String homeTax = HomeTax.valueOf(updateHometaxInfoRequest.homeTaxNum()).name();

        empRepository.updateHometaxInfo(
                customUserDetails.getId(),
                homeTax);

        return UpdateHometaxInfoResponse.of(
                true,
                "홈택스 정보가 성공적으로 업데이트 되었습니다."
        );
    }
}
