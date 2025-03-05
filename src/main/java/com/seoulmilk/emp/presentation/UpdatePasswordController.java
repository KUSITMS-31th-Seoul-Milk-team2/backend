package com.seoulmilk.emp.presentation;

import com.seoulmilk.core.infrastructure.security.CustomUserDetails;
import com.seoulmilk.core.presentation.RestResponse;
import com.seoulmilk.emp.application.UpdatePasswordService;
import com.seoulmilk.emp.dto.reqeust.UpdatePasswordRequest;
import com.seoulmilk.emp.presentation.swagger.UpdatePasswordSwagger;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Log4j2
@RestController
@RequiredArgsConstructor
@RequestMapping("/v1/emp")
public class UpdatePasswordController implements UpdatePasswordSwagger {

    private final UpdatePasswordService updatePasswordService;

    @PutMapping("/password")
    public ResponseEntity<RestResponse<String>> updatePassword(
            @AuthenticationPrincipal CustomUserDetails customUserDetails,
            @Valid @RequestBody UpdatePasswordRequest updatePasswordRequest
    ) {
        updatePasswordService.updatePassword(customUserDetails, updatePasswordRequest);
        return ResponseEntity.ok(new RestResponse<>("비밀번호가 변경되었습니다."));
    }
}
