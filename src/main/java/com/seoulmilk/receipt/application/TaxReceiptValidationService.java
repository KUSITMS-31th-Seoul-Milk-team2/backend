package com.seoulmilk.receipt.application;

import com.seoulmilk.auth.infrastructure.jwt.JwtTokenProvider;
import com.seoulmilk.emp.domain.entity.Emp;
import com.seoulmilk.emp.domain.repository.EmpRepository;
import com.seoulmilk.emp.exception.EmpErrorCode;
import com.seoulmilk.emp.infrastructure.persistence.repository.EmpJpaRepository;
import com.seoulmilk.receipt.dto.request.OcrValidationRequest;
import com.seoulmilk.receipt.dto.request.TaxReceiptValidationRequest;
import com.seoulmilk.receipt.exception.ReceiptErrorCode;
import com.seoulmilk.receipt.infrastructure.factory.TaxReceiptValidationRequestFactory;
import com.seoulmilk.receipt.presentation.dto.response.AdditionalAuthResponse;
import com.seoulmilk.receipt.presentation.dto.response.TaxReceiptValidationResponse;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwt;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.time.Duration;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
@RequiredArgsConstructor
@Log4j2
public class TaxReceiptValidationService {
    private final TaxReceiptValidationProvider taxReceiptValidationProvider;
    private final EmpRepository empRepository;
    private final RedisTemplate redisTemplate;

    @KafkaListener(topics = "${kafka.topic}", groupId = "${kafka.group-id}")
    public void listen(OcrValidationRequest ocrValidationRequest) {
        // 이것도 캐싱하고 싶은데 방법이....
        Emp emp = empRepository.findByid(ocrValidationRequest.empPk())
                .orElseThrow(() -> EmpErrorCode.NOT_EXIST_EMPLOYEE.toException());;
        log.info("[TaxReceiptValidationService] emp 전화번호 - {}", emp.getPhoneNumber());
        String cacheKey = "uuid:" + ocrValidationRequest.empPk();
        String uuid = getUUID(cacheKey);

        TaxReceiptValidationRequest taxReceiptValidationRequest = TaxReceiptValidationRequestFactory.create(
                emp, ocrValidationRequest, uuid
        );


        log.info("[TaxReceiptValidationService] 요청 데이터 - {}", taxReceiptValidationRequest);

        AdditionalAuthResponse additionalAuthResponse
                = requestAdditionalAuthentication(List.of(taxReceiptValidationRequest));

        log.info("[TaxReceiptValidationService] 받은 transactionId - {}"
                    , additionalAuthResponse.jti());
        String transactionCacheKey = "transactionId:" + ocrValidationRequest.empPk();
        String transactionId = (String) redisTemplate.opsForValue().get(transactionCacheKey);
        if(transactionId == null) {
            redisTemplate.opsForValue().set(cacheKey, additionalAuthResponse.jti());
        }
    }

    public AdditionalAuthResponse requestAdditionalAuthentication(List<TaxReceiptValidationRequest> requests) {
        return taxReceiptValidationProvider.requestAdditionalAuthentication(requests);
    }

    public List<TaxReceiptValidationResponse> retrieveValidatedTaxReceipts(String transactionId) {
        return taxReceiptValidationProvider.retrieveValidatedTaxReceipts(transactionId);
    }

    private String getUUID(String cacheKey){
        String uuid = (String) redisTemplate.opsForValue().get(cacheKey);

        if (uuid == null) {
            uuid = UUID.randomUUID().toString();
            redisTemplate.opsForValue().set(cacheKey, uuid, Duration.ofSeconds(120));
        }

        return uuid;
    }
}