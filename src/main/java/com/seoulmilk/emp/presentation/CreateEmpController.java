package com.seoulmilk.emp.presentation;

import com.seoulmilk.core.infrastructure.security.CustomUserDetails;
import com.seoulmilk.core.presentation.RestResponse;
import com.seoulmilk.emp.application.CreateEmpService;
import com.seoulmilk.emp.dto.request.GrantPrivilegeRequest;
import com.seoulmilk.emp.dto.response.CreateEmpResponse;
import com.seoulmilk.emp.presentation.swagger.CreateEmpSwagger;
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
@RequestMapping("/v1/admin")
public class CreateEmpController implements CreateEmpSwagger {
    private final CreateEmpService createEmpService;

    @PostMapping
    @RequestMapping
    public ResponseEntity<RestResponse<CreateEmpResponse>> create(
            @AuthenticationPrincipal CustomUserDetails customUserDetails,
            @Valid @RequestBody GrantPrivilegeRequest createEmpRequest
    ) {
        CreateEmpResponse createEmpResponse = createEmpService.create(customUserDetails, createEmpRequest);
        return ResponseEntity.ok(new RestResponse<>(createEmpResponse));
    }
}
