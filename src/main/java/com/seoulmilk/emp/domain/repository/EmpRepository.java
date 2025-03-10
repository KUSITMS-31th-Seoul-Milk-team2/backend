package com.seoulmilk.emp.domain.repository;

import com.seoulmilk.emp.domain.entity.Emp;

import java.util.List;
import java.util.Optional;

public interface EmpRepository {
    Emp save(Emp emp);
    Optional<Emp> findById(Long id);
    Optional<Emp> findByEmployeeId(String employeeId);
    Optional<Emp> findByEmployeeName(String employeeName);
    void deleteAll();
    void updatePassword(Long id, String newPassword);
    List<Emp> findAllByName(String employeeName);
    void grantPrivilege(Emp emp);
}
