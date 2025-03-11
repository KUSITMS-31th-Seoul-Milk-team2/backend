package com.seoulmilk.emp.domain.repository;

import com.seoulmilk.emp.domain.entity.Emp;
import com.seoulmilk.emp.dto.response.FilteredEmpResponse;
import org.springframework.data.domain.Pageable;

import java.util.List;
import java.util.Optional;

public interface EmpRepository {
    Emp save(Emp emp);

    Optional<Emp> findById(Long id);

    Optional<Emp> findByEmployeeId(String employeeId);

    Optional<Emp> findByEmployeeName(String employeeName);

    void deleteAll(List<Emp> emps);

    void updatePassword(Long id, String newPassword);

    List<Emp> findAllByName(String employeeName);

    void grantPrivilege(Emp emp);

    List<Emp> findAllByIds(List<Long> ids);

    List<FilteredEmpResponse> findAllWithNeededInfo();

    List<Emp> findAllOrderByIdDesc(Pageable pageable);

    void updateHometaxInfo(Long empPk, String hometaxName);
}
