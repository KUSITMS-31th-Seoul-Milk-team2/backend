package com.seoulmilk.core.application;

import com.seoulmilk.core.infrastructure.security.CustomUserDetails;
import com.seoulmilk.emp.domain.entity.Emp;
import com.seoulmilk.emp.domain.repository.EmpRepository;
import com.seoulmilk.emp.exception.EmpErrorCode;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class CustomUserDetailsService implements UserDetailsService {
    private final EmpRepository empRepository;

    @Override
    public UserDetails loadUserByUsername(String employeeName) throws UsernameNotFoundException {
        Emp emp = empRepository.findByEmployeeName(employeeName).orElseThrow(EmpErrorCode.NOT_EXIST_EMPLOYEE::toException);

        return new CustomUserDetails(emp);
    }

    public UserDetails loadUserById(Long id) throws UsernameNotFoundException {
        Emp emp = empRepository.findById(id).orElseThrow(EmpErrorCode.NOT_EXIST_EMPLOYEE::toException);

        return new CustomUserDetails(emp);
    }
}
