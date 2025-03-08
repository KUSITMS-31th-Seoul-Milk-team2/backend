package com.seoulmilk.emp.infrastructure.persistence.repository;

import com.seoulmilk.emp.domain.entity.Emp;
import com.seoulmilk.emp.infrastructure.persistence.jpa.entity.EmpJpaEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Optional;

public interface EmpJpaRepository extends JpaRepository<EmpJpaEntity, Long> {
    Optional<EmpJpaEntity> findByEmployeeId(String employeeId);

    Optional<EmpJpaEntity> findByName(String name);

    List<EmpJpaEntity> findAllByName(String employeeName);

    @Modifying
    @Query("""
                update EmpJpaEntity e
                set e.password = :newPassword
                where e.id = :id
            """)
    void updatePassword(Long id, String newPassword);

    @Modifying
    @Query("""
                update EmpJpaEntity e
                set e.is_signedin = CASE WHEN e.is_signedin = true THEN false ELSE true END
                where e.id = :id
            """)
    void grantPrivilege(Long id);
}
