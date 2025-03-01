package com.seoulmilk.auth.application;

import com.seoulmilk.auth.dto.request.TokenRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@RequiredArgsConstructor
@Component
public class TokenRequestFactory {

    public static TokenRequest create(String employeeId) {
        return new TokenRequest(
                employeeId
        );
    }
}
