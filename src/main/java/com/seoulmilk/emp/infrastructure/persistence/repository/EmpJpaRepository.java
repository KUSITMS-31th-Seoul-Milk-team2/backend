package com.seoulmilk.emp.infrastructure.persistence.repository;

import com.seoulmilk.emp.dto.response.FilteredEmpResponse;
import com.seoulmilk.emp.infrastructure.persistence.jpa.entity.EmpJpaEntity;
import org.jetbrains.annotations.NotNull;
import org.springframework.data.domain.Pageable;
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
                set e.isSignedIn = CASE WHEN e.isSignedIn = true THEN false ELSE true END,
                e.password = :hashedPassword
                where e.id = :id
            """)
    void grantPrivilege(Long id, String hashedPassword);


    List<EmpJpaEntity> findAllByIdIn(List<Long> ids);

    @Query("SELECT e FROM EmpJpaEntity e ORDER BY e.id DESC")
    List<EmpJpaEntity> findAllOrderByIdDesc(Pageable pageable);

    @NotNull
    @Query("""
            SELECT new com.seoulmilk.emp.dto.response.FilteredEmpResponse(
                e.id,
                e.name,
                e.employeeId,
                e.role
            )
            FROM EmpJpaEntity e
            """)
    List<FilteredEmpResponse> findAllWithNeededInfo();

    @Modifying
    @Query("""
                update EmpJpaEntity e
                set e.hometax = :hometaxName
                where e.id = :empPk
            """)
    void updateHometaxInfo(Long empPk, String hometaxName);
}
