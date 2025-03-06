package com.seoulmilk.receipt.application;

import com.seoulmilk.auth.infrastructure.jwt.JwtTokenProvider;
import com.seoulmilk.emp.domain.entity.Emp;
import com.seoulmilk.emp.domain.repository.EmpRepository;
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
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
@RequiredArgsConstructor
@Log4j2
public class TaxReceiptValidationService {
    private final TaxReceiptValidationProvider taxReceiptValidationProvider;
    private final EmpRepository empRepository;
    private final JwtTokenProvider jwtTokenProvider;

    @KafkaListener(topics = "${kafka.topic}", groupId = "${kafka.group-id}")
    public void listen(OcrValidationRequest ocrValidationRequest) {
        String uuid = UUID.randomUUID().toString();

        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

        if(authentication == null || !authentication.isAuthenticated()) {
            throw ReceiptErrorCode.FAILED_TO_SAVE_RECEIPT.toException();
        }
        log.info("[TaxReceiptValidationService] 현재 접속 사용자 정보 - {}"
                , getIdFromAuthentication(authentication));
        // TODO: 사번으로 찾도록 교체해야함
        Emp emp = empRepository.findByid(Long.parseLong(getIdFromAuthentication(authentication)))
                        .orElseThrow(() -> ReceiptErrorCode.FAILED_TO_SAVE_RECEIPT.toException());

        // TODO : 다중 요청 처리 가능하게 바꾸기
        TaxReceiptValidationRequest taxReceiptValidationRequest = TaxReceiptValidationRequestFactory.create(
                emp, ocrValidationRequest, uuid
        );

        AdditionalAuthResponse additionalAuthResponse
                = requestAdditionalAuthentication(List.of(taxReceiptValidationRequest));

        log.info("[TaxReceiptValidationService] 받은 transactionId - {}"
                    , additionalAuthResponse.jti());

        // TODO: 트랜잭션 아이디를 어떻게 클라이언트에게 제공할 것인가.
    }

    public AdditionalAuthResponse requestAdditionalAuthentication(List<TaxReceiptValidationRequest> requests) {
        return taxReceiptValidationProvider.requestAdditionalAuthentication(requests);
    }

    public List<TaxReceiptValidationResponse> retrieveValidatedTaxReceipts(String transactionId) {
        return taxReceiptValidationProvider.retrieveValidatedTaxReceipts(transactionId);
    }

    // Authentication 객체에서 Id를 추출하는 메서드
    // TODO : 마찬가지로 사번으로 바꿔야함
    private String getIdFromAuthentication(Authentication authentication) {
        String token = (String) authentication.getCredentials();
        Claims claims = jwtTokenProvider.getPayload(token);
        return claims.getSubject();
    }
}