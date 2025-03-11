package com.seoulmilk.receipt.application.query;

import com.seoulmilk.core.infrastructure.security.CustomUserDetails;
import com.seoulmilk.emp.domain.value.Role;
import com.seoulmilk.receipt.domain.InValidReceiptRepository;
import com.seoulmilk.receipt.domain.ValidReceiptRepository;
import com.seoulmilk.receipt.domain.entity.ValidReceipt;
import com.seoulmilk.receipt.dto.request.ValidResponseSearchRequest;
import com.seoulmilk.receipt.infrastructure.persistence.jpa.entity.InValidReceiptJpaEntity;
import com.seoulmilk.receipt.infrastructure.persistence.jpa.repository.InValidJpaReceiptRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
@Log4j2
public class TaxReceiptSearchService {
    private final ValidReceiptRepository validReceiptRepository;
    private final InValidJpaReceiptRepository inValidJpaReceiptRepository;

    public List<ValidReceipt> findAllValidReceipt(
            CustomUserDetails customUserDetails,
            ValidResponseSearchRequest keywords
    ){
        if(customUserDetails.getRole() == Role.ADMIN){
            log.info("[findAllValidReceipt] 현재 사용자 - 관리자");
            return validReceiptRepository.findAllBySpecificationWithAdmin(keywords);
        }else{
            log.info("[findAllValidReceipt] 현재 사용자 - 사원");
            return validReceiptRepository.findAllBySpecification(customUserDetails, keywords);
        }
    }

    public List<InValidReceiptJpaEntity> findByUserId(String userId){
        return inValidJpaReceiptRepository.findAllByEmployeeId(userId);
    }
}
