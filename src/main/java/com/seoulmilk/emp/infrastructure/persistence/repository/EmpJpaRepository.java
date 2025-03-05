package com.seoulmilk.emp.infrastructure.persistence.repository;

import com.seoulmilk.emp.infrastructure.persistence.jpa.entity.EmpJpaEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

import java.util.Optional;

public interface EmpJpaRepository extends JpaRepository<EmpJpaEntity, Long> {
    Optional<EmpJpaEntity> findByEmployeeId(String employeeId);
    Optional<EmpJpaEntity> findByName(String name);

    @Modifying
    @Query("""
        update EmpJpaEntity e
        set e.password = :newPassword
        where e.id = :id
    """)
    void updatePassword(Long id, String newPassword);
}
