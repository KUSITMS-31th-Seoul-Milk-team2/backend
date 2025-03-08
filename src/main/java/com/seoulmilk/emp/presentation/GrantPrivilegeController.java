package com.seoulmilk.emp.presentation;

import com.seoulmilk.core.infrastructure.security.CustomUserDetails;
import com.seoulmilk.core.presentation.RestResponse;
import com.seoulmilk.emp.application.GrantPrivilegeService;
import com.seoulmilk.emp.dto.request.GrantPrivilegeRequest;
import com.seoulmilk.emp.dto.response.GrantPrivilegeResponse;
import com.seoulmilk.emp.presentation.swagger.GrantPrivilegeSwagger;
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
public class GrantPrivilegeController implements GrantPrivilegeSwagger {

    private final GrantPrivilegeService grantPrivilegeService;

    @PostMapping("/grant-privilege")
    public ResponseEntity<RestResponse<GrantPrivilegeResponse>> grantPrivilege(
            @AuthenticationPrincipal CustomUserDetails customUserDetails,
            @Valid @RequestBody GrantPrivilegeRequest grantPrivilegeRequest) {

        GrantPrivilegeResponse grantPrivilegeResponse = grantPrivilegeService.grantPrivilege(customUserDetails, grantPrivilegeRequest);

        return ResponseEntity.ok(new RestResponse<>(grantPrivilegeResponse));
    }
}
