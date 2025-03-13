package com.seoulmilk.auth.presentation;

import com.seoulmilk.auth.presentation.swagger.LogoutSwagger;
import com.seoulmilk.core.presentation.RestResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseCookie;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/v1/auth")
@RequiredArgsConstructor
public class LogoutController implements LogoutSwagger {

    @PostMapping("/logout")
    public ResponseEntity<RestResponse<Void>> logout() {
        ResponseCookie cookie = ResponseCookie.from("accessToken", "")
                .path("/")
                .httpOnly(true)
                .secure(true)
                .sameSite("None")
                .maxAge(0)
                .build();

        return ResponseEntity.ok()
                .header(HttpHeaders.SET_COOKIE, cookie.toString())
                .build();
    }
}
