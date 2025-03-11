package com.seoulmilk.emp.presentation;

import com.seoulmilk.core.infrastructure.security.CustomUserDetails;
import com.seoulmilk.core.presentation.RestResponse;
import com.seoulmilk.emp.application.ReadEmpsService;
import com.seoulmilk.emp.dto.response.ReadEmpResponse;
import com.seoulmilk.emp.dto.response.ReadEmpsResponse;
import com.seoulmilk.emp.presentation.swagger.ReadEmpSwagger;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/v1/admin")
public class ReadEmpController implements ReadEmpSwagger {
    private final ReadEmpsService readEmpsService;

    @GetMapping("/all")
    public ResponseEntity<RestResponse<ReadEmpsResponse>> read(
            @AuthenticationPrincipal CustomUserDetails customUserDetails
    ) {
        ReadEmpsResponse readEmpsResponse = readEmpsService.read(customUserDetails);
        return ResponseEntity.ok(new RestResponse<>(readEmpsResponse));
    }

    @GetMapping
    public ResponseEntity<RestResponse<ReadEmpResponse>> readOneEmp(
            @AuthenticationPrincipal CustomUserDetails customUserDetails,
            @RequestParam String name
    ) {
        ReadEmpResponse readEmpResponse = readEmpsService.readEmpByName(customUserDetails, name);
        return ResponseEntity.ok(new RestResponse<>(readEmpResponse));
    }
}
