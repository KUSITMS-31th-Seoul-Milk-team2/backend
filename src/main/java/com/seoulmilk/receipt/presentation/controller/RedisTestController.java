package com.seoulmilk.receipt.presentation.controller;

import com.seoulmilk.core.presentation.RestResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/v1/receipt")
@RequiredArgsConstructor
@Log4j2
public class RedisTestController {

    private final RedisTemplate<String, String> redisTemplate;

    @GetMapping("/redis/test")
    public ResponseEntity<RestResponse<String>> redis() {
        String key = "redis";
        String value = "value";
        redisTemplate.opsForValue().set(key, value);
        log.info("[redisTest] 현재 키 - {} 현재 value - {}", key, value);

        return ResponseEntity.ok(new RestResponse<>(redisTemplate.opsForValue().getAndDelete(key)));
    }
}
