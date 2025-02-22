package com.seoulmilk.emp.domain.entity;

import com.seoulmilk.auth.domain.entity.HashedPassword;
import com.seoulmilk.emp.domain.value.Role;
import com.seoulmilk.emp.infrastructure.persistence.jpa.entity.EmpJpaEntity;
import lombok.Builder;
import lombok.Getter;

import java.time.LocalDateTime;

@Getter
@Builder
public class Emp {
    private Long id;

    private String employeeId;

    private String name;

    private String email;

    private Role role;

    private String phoneNumber;

    private HashedPassword password;

    private LocalDateTime createdAt;

    private LocalDateTime updatedAt;

    private Boolean deleted;

    public static Emp create(String name, String employeeId, String email, Role role, String phoneNumber, HashedPassword hashedPassword) {
        return Emp.builder()
                .id(null)
                .employeeId(employeeId)
                .name(name)
                .email(email)
                .role(role)
                .phoneNumber(phoneNumber)
                .password(hashedPassword)
                .build();
    }

    public static Emp toDomainEntity(EmpJpaEntity empJpaEntity) {
        return Emp.builder()
                .id(empJpaEntity.getId())
                .employeeId(empJpaEntity.getEmployeeId())
                .name(empJpaEntity.getName())
                .email(empJpaEntity.getEmail())
                .role(empJpaEntity.getRole())
                .phoneNumber(empJpaEntity.getPhoneNumber())
                .password(HashedPassword.of(empJpaEntity.getPassword()))
                .createdAt(empJpaEntity.getCreatedAt())
                .updatedAt(empJpaEntity.getUpdatedAt())
                .deleted(empJpaEntity.getDeleted())
                .build();
    }

}
