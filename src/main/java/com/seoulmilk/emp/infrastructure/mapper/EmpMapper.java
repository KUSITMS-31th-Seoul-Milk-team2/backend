package com.seoulmilk.emp.infrastructure.mapper;

import com.seoulmilk.emp.domain.entity.Emp;
import com.seoulmilk.emp.infrastructure.persistence.jpa.entity.EmpJpaEntity;
import org.springframework.stereotype.Component;

@Component
public class EmpMapper {

    public Emp toDomainEntity(EmpJpaEntity emp) {
        return Emp.toDomainEntity(emp);
    }

    public EmpJpaEntity toJpaEntity(Emp emp) {
        return EmpJpaEntity.toJpaEntity(emp);
    }
}
