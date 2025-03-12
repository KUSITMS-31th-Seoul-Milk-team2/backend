package com.seoulmilk.receipt.application;

import com.seoulmilk.core.infrastructure.security.CustomUserDetails;
import com.seoulmilk.emp.domain.entity.Emp;
import com.seoulmilk.emp.domain.repository.EmpRepository;
import com.seoulmilk.emp.exception.EmpErrorCode;
import com.seoulmilk.receipt.domain.InValidReceiptRepository;
import com.seoulmilk.receipt.domain.ValidReceiptRepository;
import com.seoulmilk.receipt.domain.entity.InValidReceipt;
import com.seoulmilk.receipt.dto.request.OcrValidationRequest;
import com.seoulmilk.receipt.dto.request.TaxReceiptValidationRequest;
import com.seoulmilk.receipt.exception.ReceiptErrorCode;
import com.seoulmilk.receipt.infrastructure.factory.ReceiptFactory;
import com.seoulmilk.receipt.infrastructure.factory.TaxReceiptValidationRequestFactory;
import com.seoulmilk.receipt.infrastructure.persistence.jpa.entity.InValidReceiptJpaEntity;
import com.seoulmilk.receipt.infrastructure.persistence.jpa.repository.InValidJpaReceiptRepository;
import com.seoulmilk.receipt.infrastructure.persistence.mapper.InValidReceiptMapper;
import com.seoulmilk.receipt.infrastructure.service.ReceiptCacheService;
import com.seoulmilk.receipt.presentation.dto.request.ValidationRequest;
import com.seoulmilk.receipt.presentation.dto.response.AdditionalAuthResponse;
import com.seoulmilk.receipt.presentation.dto.response.TaxReceiptValidationResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
@RequiredArgsConstructor
@Log4j2
public class TaxReceiptValidationService {
    private final TaxReceiptValidationProvider taxReceiptValidationProvider;
    private final EmpRepository empRepository;
    private final ReceiptCacheService receiptCacheService;
    private final InValidReceiptRepository invalidReceiptRepository;
    private final ValidReceiptRepository validReceiptRepository;
    private final RedisTemplate redisTemplate;

    @KafkaListener(topics = "${kafka.topic}", groupId = "${kafka.group-id}", concurrency = "3", errorHandler = "noRetryErrorHandler")
    public void listen(List<OcrValidationRequest> ocrValidationRequestList) {
        log.info("이벤트 결과 - {} ", ocrValidationRequestList);

        Long pk = ocrValidationRequestList.getFirst().empPk();
        log.info("현재 사용자 pk - {}", pk);

        Emp emp = getEmployee(pk);
        log.info("현재 사용자 - {}", emp.getName());

        String uuid = UUID.randomUUID().toString();
        List<TaxReceiptValidationRequest> taxReceiptValidationRequests = new ArrayList<>();

        for (OcrValidationRequest ocrValidationRequest : ocrValidationRequestList) {
            TaxReceiptValidationRequest taxReceiptValidationRequest =
                    createTaxReceiptValidationRequest(emp, ocrValidationRequest, uuid);
            taxReceiptValidationRequests.add(taxReceiptValidationRequest);
        }

        AdditionalAuthResponse additionalAuthResponse = requestAdditionalAuthentication(taxReceiptValidationRequests);

        receiptCacheService.handleTransactionId(emp.getId(), additionalAuthResponse.jti());
        log.info("저장된 트랜잭션 id key - {}, Value - {}",
                "transactionId:" + emp.getId(), redisTemplate.opsForValue().get("transactionId:" + emp.getId()));

        receiptCacheService.hadleRequestData(emp.getId(), ocrValidationRequestList);
        log.info("저장된 데이터 - {}", redisTemplate.opsForValue().get("requestData:" + emp.getId()));
    }

    private AdditionalAuthResponse requestAdditionalAuthentication(List<TaxReceiptValidationRequest> taxReceiptValidationRequests) {
        return taxReceiptValidationProvider.requestAdditionalAuthentication(taxReceiptValidationRequests);
    }

    private Emp getEmployee(Long empPk) {
        return empRepository.findById(empPk)
                .orElseThrow(EmpErrorCode.NOT_EXIST_EMPLOYEE::toException);
    }

    private TaxReceiptValidationRequest createTaxReceiptValidationRequest(
            Emp emp, OcrValidationRequest ocrValidationRequest, String uuid
    ) {
        return TaxReceiptValidationRequestFactory.create(emp, ocrValidationRequest, uuid);
    }

    private TaxReceiptValidationRequest createTaxReceiptValidationRequest(
            Emp emp, ValidationRequest validationRequest, String uuid
    ) {
        return TaxReceiptValidationRequestFactory.create(emp, validationRequest, uuid);
    }

    public Boolean retrieveValidatedTaxReceiptsWithTransactionId(CustomUserDetails customUserDetails){
        log.info("[retrieveValidatedTaxReceiptsWithTransactionId] 현재 사용자 pk - {}", customUserDetails.getId());
        Emp emp = getEmployee(customUserDetails.getId());
        log.info("[retrieveValidatedTaxReceiptsWithTransactionId] 현재 사용자 정보 - {}", emp.getName());

        String transactionCacheKey = "transactionId:" + customUserDetails.getId();
        String transactionId = receiptCacheService.getTransactionIdInRedis(transactionCacheKey);
        log.info("[retrieveValidatedTaxReceiptsWithTransactionId] 현재 트랜잭션 id - {}", transactionId);

        String dataCacheKey = "requestData:" + customUserDetails.getId();
        List<OcrValidationRequest> requestsData = receiptCacheService.getOcrValidationRequestDataInRedis(dataCacheKey);

        List<TaxReceiptValidationResponse> responses = taxReceiptValidationProvider.retrieveValidatedTaxReceipts(transactionId);
        Collections.reverse(responses);

        Boolean success = saveRecieptData(emp, requestsData, responses);

        return success;
    }

    private Boolean saveRecieptData(Emp emp, List<OcrValidationRequest> requestsData, List<TaxReceiptValidationResponse> responses){
        Boolean flag = true;
        for(int i = 0; i < responses.size(); i++) {
            OcrValidationRequest ocrValidationRequest = requestsData.get(i);
            if(responses.get(i).resAuthenticity().equals("1")){
                validReceiptRepository.save(ReceiptFactory.validReceiptCreate(emp, ocrValidationRequest));
            }else if(responses.get(i).resAuthenticity().equals("0")){
                flag = false;
                InValidReceipt inValidReceipt = ReceiptFactory.inValidReceiptCreate(emp, ocrValidationRequest);
                invalidReceiptRepository.save(inValidReceipt);
            }
        }

        return flag;
    }

    // 파일 업로드 하지 않고 5개 정보로 요청을 보낸경우
    public String requestAdditionalAuthentication(
            CustomUserDetails customUserDetails,
            List<ValidationRequest> requests
    ) {
        Emp emp = getEmployee(customUserDetails.getId());

        List<TaxReceiptValidationRequest> taxReceiptValidationRequestList = new ArrayList<>();
        String uuid = UUID.randomUUID().toString();
        for (ValidationRequest validationRequest : requests) {
            TaxReceiptValidationRequest taxReceiptValidationRequest =
                    createTaxReceiptValidationRequest(emp, validationRequest, uuid);
            taxReceiptValidationRequestList.add(taxReceiptValidationRequest);
        }
        log.info("현재 요청 보내는 데이터 - {}", taxReceiptValidationRequestList);
        AdditionalAuthResponse additionalAuthResponse =
            taxReceiptValidationProvider.requestAdditionalAuthentication(taxReceiptValidationRequestList);

        return additionalAuthResponse.jti();
    }

    public Boolean retrieveValidatedTaxReceipts(
            CustomUserDetails customUserDetails, List<Long> receiptPks, String transactionId
    ) {
        List<TaxReceiptValidationResponse> responses =
                taxReceiptValidationProvider.retrieveValidatedTaxReceipts(transactionId);

        Collections.reverse(responses);

        Boolean success = updateInvalidReceiptData(getEmployee(customUserDetails.getId()), receiptPks, responses);

        return success;
    }

    public Boolean updateInvalidReceiptData(Emp emp, List<Long> receiptPks, List<TaxReceiptValidationResponse> responses){
        Boolean flag = true;

        for(int i = 0; i < responses.size(); i++) {
            if (i >= receiptPks.size()) {
                throw new IllegalStateException("receiptPks와 responses의 개수가 일치하지 않습니다.");
            }
            Long pk = receiptPks.get(i);
            if(responses.get(i).resAuthenticity().equals("1")){
                InValidReceipt inValidReceipt = invalidReceiptRepository.findById(pk)
                        .orElseThrow(ReceiptErrorCode.NOT_EXIST_RECEIPT::toException);
                invalidReceiptRepository.deleteById(pk);
                validReceiptRepository.save(ReceiptFactory.validReceiptCreate(emp, inValidReceipt));
            }else if(responses.get(i).resAuthenticity().equals("0")){
                flag = false;
            }
        }

        return flag;
    }
}
