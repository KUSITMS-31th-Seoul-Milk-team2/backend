package com.seoulmilk.auth.presentation;

import com.seoulmilk.auth.application.LoginService;
import com.seoulmilk.auth.application.TokenRequestFactory;
import com.seoulmilk.auth.application.TokenService;
import com.seoulmilk.auth.infrastructure.jwt.JwtProperties;
import com.seoulmilk.auth.presentation.dto.request.LoginRequest;
import com.seoulmilk.auth.presentation.dto.response.LoginResponse;
import com.seoulmilk.auth.presentation.swagger.LoginSwagger;
import com.seoulmilk.core.presentation.RestResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseCookie;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/v1/auth")
@RequiredArgsConstructor
public class LoginController implements LoginSwagger {

    private final LoginService loginService;
    private final TokenService tokenService;
    private final JwtProperties jwtProperties;

    @Override
    @PostMapping("/login")
    public ResponseEntity<RestResponse<LoginResponse>> login(@Valid @RequestBody LoginRequest request) {
        LoginResponse loginResponse = loginService.login(request);
        String accessToken = tokenService.provideAccessToken(TokenRequestFactory.create(request.employeeId()));
        long accessCookieMaxAge = jwtProperties.getAccess().getExpiration() / 1000;

        ResponseCookie cookie = ResponseCookie.from("accessToken", accessToken)
                .path("/")
                .httpOnly(false)
                .maxAge(accessCookieMaxAge)
                .build();

        return ResponseEntity.ok()
                .header(HttpHeaders.SET_COOKIE, cookie.toString())
                .body(new RestResponse<>(loginResponse));
    }
}
