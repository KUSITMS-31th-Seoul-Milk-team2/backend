package com.seoulmilk.receipt.domain;

import com.seoulmilk.core.infrastructure.security.CustomUserDetails;
import com.seoulmilk.receipt.domain.entity.ValidReceipt;
import com.seoulmilk.receipt.dto.request.UpdateValidReceiptRequest;
import com.seoulmilk.receipt.dto.request.ValidResponseSearchRequest;

import java.util.List;
import java.util.Optional;

public interface ValidReceiptRepository {
    ValidReceipt save(ValidReceipt validReceipt);

    Optional<ValidReceipt> findById(Long id);
    Optional<ValidReceipt> findByIssueId(String issueId);

    void deleteAll();
    void deleteById(Long pk);

    int update(UpdateValidReceiptRequest updateValidReceiptRequest);

    List<ValidReceipt> findAllBySpecificationWithAdmin(
            ValidResponseSearchRequest validResponseSearchRequest
    );

    List<ValidReceipt> findAllBySpecification(
            CustomUserDetails customUserDetails,
            ValidResponseSearchRequest validResponseSearchRequest
    );
}
