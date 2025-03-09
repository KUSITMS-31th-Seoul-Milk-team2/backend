package com.seoulmilk.receipt.application;

import com.seoulmilk.core.infrastructure.security.CustomUserDetails;
import com.seoulmilk.emp.domain.entity.Emp;
import com.seoulmilk.emp.domain.repository.EmpRepository;
import com.seoulmilk.emp.exception.EmpErrorCode;
import com.seoulmilk.receipt.domain.InValidReceiptRepository;
import com.seoulmilk.receipt.domain.ValidReceiptRepository;
import com.seoulmilk.receipt.domain.entity.InValidReceipt;
import com.seoulmilk.receipt.domain.entity.ValidReceipt;
import com.seoulmilk.receipt.domain.value.Arap;
import com.seoulmilk.receipt.dto.request.OcrValidationRequest;
import com.seoulmilk.receipt.dto.request.TaxReceiptValidationRequest;
import com.seoulmilk.receipt.exception.ReceiptValidationErrorCode;
import com.seoulmilk.receipt.infrastructure.factory.TaxReceiptValidationRequestFactory;
import com.seoulmilk.receipt.presentation.dto.request.ValidationRequest;
import com.seoulmilk.receipt.presentation.dto.response.AdditionalAuthResponse;
import com.seoulmilk.receipt.presentation.dto.response.TaxReceiptValidationResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;

import java.time.Duration;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
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

    @KafkaListener(topics = "${kafka.topic}", groupId = "${kafka.group-id}", concurrency = "3", errorHandler = "noRetryErrorHandler")
    public void listen(List<OcrValidationRequest> ocrValidationRequestList) {
        log.info("이벤트 결과:  " + ocrValidationRequestList);

        Long pk = ocrValidationRequestList.get(0).empPk();

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

        log.info("추가인증 정보 요청 시작");
        AdditionalAuthResponse additionalAuthResponse = requestAdditionalAuthentication(taxReceiptValidationRequests);
        log.info("추가인증 정보 요청 끝");
        handleTransactionId(emp.getId(), additionalAuthResponse.jti());
        log.info("레디스에 저장된 트랜잭션 id - {}", redisTemplate.opsForValue().get("transactionId:" + pk));

        hadleRequestData(emp.getId(), ocrValidationRequestList);
        log.info("레디스에 저장 된 데이터 - {}", redisTemplate.opsForValue().get("requestData:" + pk));
    }

    private AdditionalAuthResponse requestAdditionalAuthentication(List<TaxReceiptValidationRequest> taxReceiptValidationRequests) {
        return taxReceiptValidationProvider.requestAdditionalAuthentication(taxReceiptValidationRequests);
    }

    private Emp getEmployee(Long empPk) {
        return empRepository.findByid(empPk)
                .orElseThrow(() -> EmpErrorCode.NOT_EXIST_EMPLOYEE.toException());
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

    private void handleTransactionId(Long empPk, String transactionId) {
        String transactionCacheKey = "transactionId:" + empPk;
        if (redisTemplate.opsForValue().get(transactionCacheKey) == null) {
            redisTemplate.opsForValue().set(transactionCacheKey, transactionId, Duration.ofMinutes(3));
        }
    }

    private void hadleRequestData(Long pk, List<OcrValidationRequest> requests) {
        String dataCacheKey = "requestData:" + pk;
        if (redisTemplate.opsForValue().get(dataCacheKey) == null) {
            redisTemplate.opsForValue().set(dataCacheKey, requests, Duration.ofMinutes(3));
        }
    }

    public AdditionalAuthResponse requestAdditionalAuthentication(
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

        return taxReceiptValidationProvider.requestAdditionalAuthentication(taxReceiptValidationRequestList);
    }

    public List<TaxReceiptValidationResponse> retrieveValidatedTaxReceiptsWithTransactionId(CustomUserDetails customUserDetails){
        log.info("[retrieveValidatedTaxReceiptsWithTransactionId] 현재 사용자 pk - {}", customUserDetails.getId());
        Emp emp = getEmployee(customUserDetails.getId());
        log.info("[retrieveValidatedTaxReceiptsWithTransactionId] 현재 사용자 정보 - {}", emp.getName());

        String transactionCacheKey = "transactionId:" + customUserDetails.getId();
        if (redisTemplate.opsForValue().get(transactionCacheKey) == null) {
            throw ReceiptValidationErrorCode.NOT_EXIST_TXID.toException();
        }
        String transactionId = (String) redisTemplate.opsForValue().getAndDelete(transactionCacheKey);
        log.info("[retrieveValidatedTaxReceiptsWithTransactionId] 현재 트랜잭션 id - {}", transactionId);


        String dataCacheKey = "requestData:" + customUserDetails.getId();
        if (redisTemplate.opsForValue().get(dataCacheKey) == null) {
            throw ReceiptValidationErrorCode.ERROR_TO_GET_DATA.toException();
        }
        List<OcrValidationRequest> requestsData = (List<OcrValidationRequest>) redisTemplate.opsForValue().getAndDelete(dataCacheKey);
        log.info("[retrieveValidatedTaxReceiptsWithTransactionId] 현재 레디스에서 찾은 데이터 - {}", requestsData);

        List<TaxReceiptValidationResponse> responses = taxReceiptValidationProvider.retrieveValidatedTaxReceipts(transactionId);

        saveRecieptData(emp, requestsData, responses);

        return responses;
    }

    private void saveRecieptData(Emp emp, List<OcrValidationRequest> requestsData, List<TaxReceiptValidationResponse> responses){
        String fileUrl = null;
        LocalDateTime now = LocalDateTime.now();
        String erdat = now.format(DateTimeFormatter.ofPattern("yyyy-MM-dd"));
        String erzet = now.format(DateTimeFormatter.ofPattern("HH:mm:ss"));

        for(int i = 0; i < responses.size(); i++) {
            OcrValidationRequest ocrValidationRequest = requestsData.get(i);
            if(responses.get(i).resAuthenticity().equals("1")){
                validReceiptRepository.save(ValidReceipt.create(
                        emp.getEmployeeId(),
                        Arap.AR,
                        ocrValidationRequest.taxValidationInfo().approvalNo(),
                        ocrValidationRequest.taxValidationInfo().reportingDate(),
                        ocrValidationRequest.taxValidationInfo().supplierRegNumber(),
                        ocrValidationRequest.taxValidationInfo().supplierName(),
                        ocrValidationRequest.taxValidationInfo().contractorRegNumber(),
                        ocrValidationRequest.taxValidationInfo().contractorName(),
                        getChargeTotal(ocrValidationRequest.taxValidationInfo().grandTotal(), ocrValidationRequest.taxValidationInfo().taxTotal()),
                        Integer.parseInt(ocrValidationRequest.taxValidationInfo().taxTotal()),
                        Integer.parseInt(ocrValidationRequest.taxValidationInfo().grandTotal()),
                        erdat,
                        erzet,
                        ocrValidationRequest.fileUrl()
                ));
            }else if(responses.get(i).resAuthenticity().equals("0")){
                invalidReceiptRepository.save(InValidReceipt.create(
                        emp.getEmployeeId(),
                        Arap.AR,
                        ocrValidationRequest.taxValidationInfo().approvalNo(),
                        ocrValidationRequest.taxValidationInfo().reportingDate(),
                        ocrValidationRequest.taxValidationInfo().supplierRegNumber(),
                        ocrValidationRequest.taxValidationInfo().supplierName(),
                        ocrValidationRequest.taxValidationInfo().contractorRegNumber(),
                        ocrValidationRequest.taxValidationInfo().contractorName(),
                        Integer.parseInt(ocrValidationRequest.taxValidationInfo().supplyValue()),
                        Integer.parseInt(ocrValidationRequest.taxValidationInfo().taxTotal()),
                        Integer.parseInt(ocrValidationRequest.taxValidationInfo().grandTotal()),
                        erdat,
                        erzet,
                        ocrValidationRequest.fileUrl()
                ));
            }
        }
    }

    private Integer getChargeTotal(String grandTotal, String taxTotal) {
        return Integer.parseInt(grandTotal) - Integer.parseInt(taxTotal);
    }

    public List<TaxReceiptValidationResponse> retrieveValidatedTaxReceipts(
            Long empPk, List<OcrValidationRequest> requestsData, String transactionId
    ) {
        List<TaxReceiptValidationResponse> responses =
                taxReceiptValidationProvider.retrieveValidatedTaxReceipts(transactionId);

        saveRecieptData(getEmployee(empPk), requestsData, responses);

        return responses;
    }
}
