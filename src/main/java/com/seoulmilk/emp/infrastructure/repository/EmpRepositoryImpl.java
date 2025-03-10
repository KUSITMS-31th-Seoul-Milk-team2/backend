package com.seoulmilk.emp.infrastructure.repository;

import com.seoulmilk.core.exception.error.GlobalErrorCode;
import com.seoulmilk.emp.domain.entity.Emp;
import com.seoulmilk.emp.domain.repository.EmpRepository;
import com.seoulmilk.emp.exception.EmpErrorCode;
import com.seoulmilk.emp.infrastructure.mapper.EmpMapper;
import com.seoulmilk.emp.infrastructure.persistence.jpa.entity.EmpJpaEntity;
import com.seoulmilk.emp.infrastructure.persistence.repository.EmpJpaRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Repository
@RequiredArgsConstructor
@Log4j2
public class EmpRepositoryImpl implements EmpRepository {

    private final EmpMapper empMapper;
    private final EmpJpaRepository empJpaRepository;

    @Override
    public Emp save(Emp emp) {
        EmpJpaEntity empJpaEntity = empMapper.toJpaEntity(emp);
        if (empJpaEntity == null) {
            throw EmpErrorCode.FAILED_TO_SAVE_EMPLOYEE.toException();
        }
        empJpaRepository.save(empJpaEntity);
        return empMapper.toDomainEntity(empJpaEntity);
    }

    @Override
    public Optional<Emp> findById(Long id) {
        return empJpaRepository.findById(id).map(empMapper::toDomainEntity);
    }

    @Override
    public Optional<Emp> findByEmployeeId(String employeeId) {
        return empJpaRepository.findByEmployeeId(employeeId).map(empMapper::toDomainEntity);
    }

    @Override
    public Optional<Emp> findByEmployeeName(String employeeName) {
        return empJpaRepository.findByName(employeeName).map(empMapper::toDomainEntity);
    }

    @Override
    public void deleteAll() {
        empJpaRepository.deleteAll();
    }

    @Override
    public void updatePassword(Long id, String newPassword) {
        try {
            empJpaRepository.updatePassword(id, newPassword);
        } catch (Exception e) {
            log.error("[EmpRepositoryImpl] updatePassword 쿼리 실행 중 에러 발생 : {}", e.getMessage());
            throw GlobalErrorCode.INTERNAL_SERVER_ERROR.toException();
        }
    }

    @Override
    public List<Emp> findAllByName(String employeeName) {
        return empJpaRepository.findAllByName(employeeName)
                .stream()
                .map(empMapper::toDomainEntity)
                .collect(Collectors.toList());
    }

    @Override
    public void grantPrivilege(Emp emp) {
        try {
            EmpJpaEntity empJpaEntity = empMapper.toJpaEntity(emp);
            if (empJpaEntity == null) {
                log.error("[EmpRepositoryImpl] 사원 엔티티를 JPA 엔티티로 변환 도중 에러 발생 ");
                throw EmpErrorCode.FAILED_TO_SAVE_EMPLOYEE.toException();
            }
            empJpaRepository.grantPrivilege(empJpaEntity.getId());
        } catch (Exception e) {
            log.error("[EmpRepositoryImpl] grantPrivilege 쿼리 실행 중 에러 발생 : {}", e.getMessage());
            throw GlobalErrorCode.INTERNAL_SERVER_ERROR.toException();
        }
    }
}
