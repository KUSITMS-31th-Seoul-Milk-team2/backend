package com.seoulmilk.emp.domain.repository;

import com.seoulmilk.emp.domain.entity.Emp;

import java.util.Optional;

public interface EmpRepository {
    Emp save(Emp emp);
    Optional<Emp> findByEmployeeId(String employeeId);
}
