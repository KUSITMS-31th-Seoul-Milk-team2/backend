package com.seoulmilk.receipt.application;

import com.seoulmilk.core.infrastructure.security.CustomUserDetails;
import com.seoulmilk.emp.domain.entity.Emp;
import com.seoulmilk.emp.domain.repository.EmpRepository;
import com.seoulmilk.emp.exception.EmpErrorCode;
import com.seoulmilk.receipt.domain.InValidReceiptRepository;
import com.seoulmilk.receipt.domain.ValidReceiptRepository;
import com.seoulmilk.receipt.dto.request.OcrValidationRequest;
import com.seoulmilk.receipt.dto.request.TaxReceiptValidationRequest;
import com.seoulmilk.receipt.exception.ReceiptValidationErrorCode;
import com.seoulmilk.receipt.infrastructure.factory.TaxReceiptValidationRequestFactory;
import com.seoulmilk.receipt.presentation.dto.response.AdditionalAuthResponse;
import com.seoulmilk.receipt.presentation.dto.response.TaxReceiptValidationResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;

import java.time.Duration;
import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
@Log4j2
public class TaxReceiptValidationService {
    private final TaxReceiptValidationProvider taxReceiptValidationProvider;
    private final EmpRepository empRepository;
    private final RedisTemplate redisTemplate;
    private final InValidReceiptRepository invalidReceiptRepository;
    private final ValidReceiptRepository validReceiptRepository;

    @KafkaListener(topics = "${kafka.topic}", groupId = "${kafka.group-id}")
    public void listen(OcrValidationRequest ocrValidationRequest) {
        Emp emp = getEmployee(ocrValidationRequest.empPk());

        String uuid = getUUID("uuid:" + ocrValidationRequest.empPk());
        TaxReceiptValidationRequest taxReceiptValidationRequest = createTaxReceiptValidationRequest(emp, ocrValidationRequest, uuid);

        AdditionalAuthResponse additionalAuthResponse = requestAdditionalAuthentication(List.of(taxReceiptValidationRequest));

        handleTransactionId(ocrValidationRequest.empPk(), additionalAuthResponse.jti());
    }

    private Emp getEmployee(Long empPk) {
        return empRepository.findByid(empPk)
                .orElseThrow(() -> EmpErrorCode.NOT_EXIST_EMPLOYEE.toException());
    }

    private TaxReceiptValidationRequest createTaxReceiptValidationRequest(Emp emp, OcrValidationRequest ocrValidationRequest, String uuid) {
        return TaxReceiptValidationRequestFactory.create(emp, ocrValidationRequest, uuid);
    }

    private void handleTransactionId(Long empPk, String transactionId) {
        String transactionCacheKey = "transactionId:" + empPk;
        if (redisTemplate.opsForValue().get(transactionCacheKey) == null) {
            redisTemplate.opsForValue().set(transactionCacheKey, transactionId, Duration.ofMinutes(5));
        }
    }

    private String getUUID(String cacheKey) {
        String uuid = (String) redisTemplate.opsForValue().get(cacheKey);
        if (uuid == null) {
            uuid = UUID.randomUUID().toString();
            redisTemplate.opsForValue().set(cacheKey, uuid, Duration.ofMinutes(3));
        }
        return uuid;
    }

    public AdditionalAuthResponse requestAdditionalAuthentication(List<TaxReceiptValidationRequest> requests) {
        return taxReceiptValidationProvider.requestAdditionalAuthentication(requests);
    }

    public List<TaxReceiptValidationResponse> retrieveValidatedTaxReceiptsWithTransactionId(CustomUserDetails customUserDetails){
        String transactionCacheKey = "transactionId:" + customUserDetails.getId();
        String transactionId = (String) redisTemplate.opsForValue().get(transactionCacheKey);
        if (redisTemplate.opsForValue().get(transactionCacheKey) == null) {
            throw ReceiptValidationErrorCode.NOT_EXIST_TXID.toException();
        }
        return taxReceiptValidationProvider.retrieveValidatedTaxReceipts(transactionId);
//        List<TaxReceiptValidationResponse> responses =
//        for(TaxReceiptValidationResponse response : responses){
//
//        }
    }

    public List<TaxReceiptValidationResponse> retrieveValidatedTaxReceipts(String transactionId) {
        return taxReceiptValidationProvider.retrieveValidatedTaxReceipts(transactionId);
    }
}
