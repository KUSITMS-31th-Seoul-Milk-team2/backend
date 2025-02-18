package com.seoulmilk.core.presentation;

import com.seoulmilk.core.presentation.swagger.HealthSwagger;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class HealthController implements HealthSwagger {
    @GetMapping("/health")
    public ResponseEntity<RestResponse<String>> health() {
        return ResponseEntity.ok(new RestResponse<>("OK"));
    }
}
