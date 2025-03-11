package com.seoulmilk.emp.application;

import com.seoulmilk.core.infrastructure.security.CustomUserDetails;
import com.seoulmilk.emp.domain.entity.Emp;
import com.seoulmilk.emp.domain.repository.EmpRepository;
import com.seoulmilk.emp.domain.value.Role;
import com.seoulmilk.emp.dto.request.DeleteEmpsRequest;
import com.seoulmilk.emp.dto.response.DeleteEmpResponse;
import com.seoulmilk.emp.exception.AdminErrorCode;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
@Log4j2
public class DeleteEmpService {

    private final EmpRepository empRepository;

    @Transactional
    public DeleteEmpResponse delete(
            CustomUserDetails customUserDetails,
            DeleteEmpsRequest deleteEmpsRequest
    ) {
        if (customUserDetails.getRole() != Role.ADMIN) {
            log.error("[DeleteEmpService.delete] 관리자만 사원을 삭제할 수 있습니다. 접근 유저 PK: {}", customUserDetails.getId());
            throw AdminErrorCode.NOT_ADMIN_EXCEPTION.toException();
        }
        List<Emp> emps = empRepository.findAllByIds(deleteEmpsRequest.ids());

        if (emps.size() != deleteEmpsRequest.ids().size()) {
            log.error("[DeleteEmpService.delete] 삭제할 수 없는 회원이 포함되어 있습니다. 삭제할 수 없는 회원 PK: {}", deleteEmpsRequest.ids());
            throw AdminErrorCode.EMP_NOT_FOUND.toException();
        }

        empRepository.deleteAll(emps);
        return DeleteEmpResponse.of(
                true,
                "회원 삭제 성공"
        );
    }
}
