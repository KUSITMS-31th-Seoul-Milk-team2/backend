package com.seoulmilk.auth.presentation;

import com.seoulmilk.auth.application.LoginService;
import com.seoulmilk.auth.presentation.dto.request.LoginRequest;
import com.seoulmilk.auth.presentation.dto.response.LoginResponse;
import com.seoulmilk.auth.presentation.swagger.LoginSwagger;
import com.seoulmilk.core.presentation.RestResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
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

    @Override
    @PostMapping("/login")
    public ResponseEntity<RestResponse<LoginResponse>> login(@Valid @RequestBody LoginRequest request) {
        LoginResponse loginResponse = loginService.login(request);
        return ResponseEntity.ok(new RestResponse<>(loginResponse));
    }
}
