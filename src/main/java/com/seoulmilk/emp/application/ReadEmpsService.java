package com.seoulmilk.emp.application;

import com.seoulmilk.core.infrastructure.security.CustomUserDetails;
import com.seoulmilk.emp.domain.entity.Emp;
import com.seoulmilk.emp.domain.repository.EmpRepository;
import com.seoulmilk.emp.domain.value.Role;
import com.seoulmilk.emp.dto.response.FilteredEmpResponse;
import com.seoulmilk.emp.dto.response.ReadEmpResponse;
import com.seoulmilk.emp.dto.response.ReadEmpsResponse;
import com.seoulmilk.emp.exception.AdminErrorCode;
import com.seoulmilk.emp.exception.EmpErrorCode;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
@Log4j2
public class ReadEmpsService {
    private final EmpRepository empRepository;

    public ReadEmpsResponse read(CustomUserDetails customUserDetails) {
        if (customUserDetails.getRole() != Role.ADMIN) {
            log.error("[ReadEmpsService.read] 관리자만 사원 목록을 조회할 수 있습니다. 접근 유저 PK: {}", customUserDetails.getId());
            throw AdminErrorCode.NOT_ADMIN_EXCEPTION.toException();
        }
        List<FilteredEmpResponse> allEmps = empRepository.findAllWithNeededInfo();

        return ReadEmpsResponse.of(allEmps);
    }

    public ReadEmpResponse readEmpByName(
            CustomUserDetails customUserDetails,
            String name
    ) {
        if (customUserDetails.getRole() != Role.ADMIN) {
            log.error("[ReadEmpsService.readOneEmp] 관리자만 사원을 조회할 수 있습니다. 접근 유저 PK: {}", customUserDetails.getId());
            throw AdminErrorCode.NOT_ADMIN_EXCEPTION.toException();
        }
        Emp emp = empRepository.findByEmployeeName(name).orElseThrow(
                EmpErrorCode.NOT_EXIST_EMPLOYEE::toException
        );

        return ReadEmpResponse.of(emp);
    }
}
