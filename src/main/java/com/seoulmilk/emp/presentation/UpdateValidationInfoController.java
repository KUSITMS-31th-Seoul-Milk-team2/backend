package com.seoulmilk.emp.presentation;

import com.seoulmilk.core.infrastructure.security.CustomUserDetails;
import com.seoulmilk.core.presentation.RestResponse;
import com.seoulmilk.emp.application.UpdateValidationInfoService;
import com.seoulmilk.emp.dto.request.UpdateHometaxInfoRequest;
import com.seoulmilk.emp.dto.response.UpdateHometaxInfoResponse;
import com.seoulmilk.emp.presentation.swagger.UpdateValidationInfoSwagger;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/v1/emp")
public class UpdateValidationInfoController implements UpdateValidationInfoSwagger {

    private final UpdateValidationInfoService updateValidationInfoService;

    @PutMapping("/hometax")
    public ResponseEntity<RestResponse<UpdateHometaxInfoResponse>> updateHometaxInfo(
            @AuthenticationPrincipal CustomUserDetails customUserDetails,
            @Valid @RequestBody UpdateHometaxInfoRequest updateHometaxInfoRequest
    ) {
        UpdateHometaxInfoResponse updateHometaxInfoResponse = updateValidationInfoService.updateHometaxInfo(customUserDetails, updateHometaxInfoRequest);
        return ResponseEntity.ok(new RestResponse<>(updateHometaxInfoResponse));
    }
}
