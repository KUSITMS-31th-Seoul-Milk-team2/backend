package com.seoulmilk.receipt.infrastructure.service;

import com.seoulmilk.receipt.dto.request.OcrValidationRequest;
import com.seoulmilk.receipt.exception.ReceiptValidationErrorCode;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Component;

import java.time.Duration;
import java.util.List;

@Component
@RequiredArgsConstructor
@Log4j2
public class ReceiptCacheService {
    private final RedisTemplate redisTemplate;

    public String getTransactionIdInRedis(String cacheKey){
        return (String) redisTemplate.opsForValue().getAndDelete(cacheKey);
    }

    public List<OcrValidationRequest> getOcrValidationRequestDataInRedis(String cacheKey){
        return (List<OcrValidationRequest>) redisTemplate.opsForValue().getAndDelete(cacheKey);
    }

    public void handleTransactionId(Long empPk, String transactionId) {
        String transactionCacheKey = "transactionId:" + empPk;
        if (redisTemplate.opsForValue().get(transactionCacheKey) == null) {
            redisTemplate.opsForValue().set(transactionCacheKey, transactionId, Duration.ofMinutes(3));
        }
    }

    public void hadleRequestData(Long pk, List<OcrValidationRequest> requests) {
        String dataCacheKey = "requestData:" + pk;
        if (redisTemplate.opsForValue().get(dataCacheKey) == null) {
            redisTemplate.opsForValue().set(dataCacheKey, requests, Duration.ofMinutes(3));
        }
    }
}
