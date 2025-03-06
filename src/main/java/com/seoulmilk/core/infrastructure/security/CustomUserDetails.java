package com.seoulmilk.core.infrastructure.security;

import com.seoulmilk.emp.domain.entity.Emp;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Collection;
import java.util.List;

public record CustomUserDetails(Emp emp) implements UserDetails {
    @Override
    public boolean isAccountNonExpired() {
        return UserDetails.super.isAccountNonExpired();
    }

    @Override
    public boolean isAccountNonLocked() {
        return UserDetails.super.isAccountNonLocked();
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return UserDetails.super.isCredentialsNonExpired();
    }

    @Override
    public boolean isEnabled() {
        return UserDetails.super.isEnabled();
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return List.of();
    }

    @Override
    public String getPassword() {
        return emp.getPassword().getValue();
    }

    @Override
    public String getUsername() {
        return emp.getName();
    }

    public String getEmployeeId() {
        return emp.getEmployeeId();
    }

    public Long getId() {
        return emp.getId();
    }

    public String getEmail() {
        return emp.getEmail();
    }
}
