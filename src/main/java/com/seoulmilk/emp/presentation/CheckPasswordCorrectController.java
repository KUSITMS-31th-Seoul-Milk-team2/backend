package com.seoulmilk.emp.presentation;

import com.seoulmilk.core.infrastructure.security.CustomUserDetails;
import com.seoulmilk.core.presentation.RestResponse;
import com.seoulmilk.emp.application.CheckPasswordCorrectService;
import com.seoulmilk.emp.dto.request.CheckPasswordCorrectRequest;
import com.seoulmilk.emp.dto.response.CheckPasswordCorrectResponse;
import com.seoulmilk.emp.presentation.swagger.CheckPasswordCorrectSwagger;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/v1/emp")
public class CheckPasswordCorrectController implements CheckPasswordCorrectSwagger {
    private final CheckPasswordCorrectService checkPasswordCorrectService;

    @Override
    @PostMapping("/check-password-correct")
    public ResponseEntity<RestResponse<CheckPasswordCorrectResponse>> checkPasswordCorrect(
            @AuthenticationPrincipal CustomUserDetails customUserDetails,
            @Valid @RequestBody CheckPasswordCorrectRequest checkPasswordCorrectRequest
    ) {
        CheckPasswordCorrectResponse checkPasswordCorrectResponse = checkPasswordCorrectService.checkPasswordCorrect(customUserDetails, checkPasswordCorrectRequest);
        return ResponseEntity.ok(new RestResponse<>(checkPasswordCorrectResponse));
    }
}
