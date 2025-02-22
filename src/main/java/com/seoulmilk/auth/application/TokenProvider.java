package com.seoulmilk.auth.application;

import com.seoulmilk.emp.domain.entity.Emp;

public interface TokenProvider {
    String provideAccessToken(Emp emp);
}
