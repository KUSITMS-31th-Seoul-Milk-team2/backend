package com.seoulmilk.emp.infrastructure.repository;

import com.seoulmilk.emp.domain.entity.Emp;
import com.seoulmilk.emp.domain.repository.EmpRepository;
import com.seoulmilk.emp.exception.EmpErrorCode;
import com.seoulmilk.emp.infrastructure.mapper.EmpMapper;
import com.seoulmilk.emp.infrastructure.persistence.jpa.entity.EmpJpaEntity;
import com.seoulmilk.emp.infrastructure.persistence.repository.EmpJpaRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.stereotype.Repository;

import java.util.Optional;

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
    public Optional<Emp> findByEmployeeId(String employeeId) {
        return empJpaRepository.findByEmployeeId(employeeId).map(empMapper::toDomainEntity);
    }
}
