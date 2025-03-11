package com.seoulmilk.emp.presentation;

import com.seoulmilk.core.infrastructure.security.CustomUserDetails;
import com.seoulmilk.core.presentation.RestResponse;
import com.seoulmilk.emp.application.DeleteEmpService;
import com.seoulmilk.emp.dto.request.DeleteEmpsRequest;
import com.seoulmilk.emp.dto.response.DeleteEmpResponse;
import com.seoulmilk.emp.presentation.swagger.DeleteEmpsSwagger;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/v1/admin")
@RequiredArgsConstructor
public class DeleteEmpsController implements DeleteEmpsSwagger {
    private final DeleteEmpService deleteEmpsService;

    @DeleteMapping
    public ResponseEntity<RestResponse<DeleteEmpResponse>> delete(
            @AuthenticationPrincipal CustomUserDetails customUserDetails,
            @RequestBody DeleteEmpsRequest deleteEmpsRequest
    ) {
        DeleteEmpResponse deleteEmpResponse = deleteEmpsService.delete(
                customUserDetails,
                deleteEmpsRequest
        );
        return ResponseEntity.ok(new RestResponse<>(deleteEmpResponse));
    }
}
