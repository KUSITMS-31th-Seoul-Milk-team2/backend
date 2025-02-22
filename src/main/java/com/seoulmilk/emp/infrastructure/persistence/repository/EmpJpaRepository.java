package com.seoulmilk.emp.infrastructure.persistence.repository;

import com.seoulmilk.emp.infrastructure.persistence.jpa.entity.EmpJpaEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface EmpJpaRepository extends JpaRepository<EmpJpaEntity, Long> {
    Optional<EmpJpaEntity> findByEmployeeId(String employeeId);
}
