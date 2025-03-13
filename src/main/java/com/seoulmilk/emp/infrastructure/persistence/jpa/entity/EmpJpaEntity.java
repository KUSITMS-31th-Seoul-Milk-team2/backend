package com.seoulmilk.emp.infrastructure.persistence.jpa.entity;

import com.seoulmilk.core.infrastructure.jpa.entity.BaseLongIdEntity;
import com.seoulmilk.emp.domain.entity.Emp;
import com.seoulmilk.emp.domain.value.HomeTax;
import com.seoulmilk.emp.domain.value.Role;
import com.seoulmilk.emp.domain.value.Telecom;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

import java.util.Optional;

@Entity
@Getter
@SuperBuilder
@Table(name = "EMP")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class EmpJpaEntity extends BaseLongIdEntity {
    @Column(nullable = false, length = 30)
    private String name;

    @Column(nullable = false, unique = true, length = 40)
    private String employeeId;

    @Column(unique = true, length = 40)
    private String email;

    @Enumerated(EnumType.STRING)
    private Role role;

    @Column(unique = true, length = 30)
    private String phoneNumber;

    private String password;

    private String birthday;

    @Enumerated(EnumType.STRING)
    private Telecom telecom;

    @Enumerated(EnumType.STRING)
    private HomeTax hometax;

    @Builder.Default
    private Boolean isSignedIn = false;

    public static EmpJpaEntity toJpaEntity(Emp emp) {
        return EmpJpaEntity.builder()
                .id(emp.getId())
                .employeeId(emp.getEmployeeId())
                .name(emp.getName())
                .role(emp.getRole())
                .email(emp.getEmail())
                .phoneNumber(emp.getPhoneNumber())
                .password(emp.getPassword().getValue())
                .birthday(emp.getBirthday())
                .telecom(emp.getTelecom())
                .hometax(emp.getHometax())
                .isSignedIn(emp.getIsSignedIn())
                .createdAt(emp.getCreatedAt())
                .updatedAt(emp.getUpdatedAt())
                .deleted(Optional.ofNullable(emp.getDeleted()).orElse(false))
                .build();
    }
}
