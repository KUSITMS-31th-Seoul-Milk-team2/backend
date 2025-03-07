package com.seoulmilk.receipt.domain;

import com.seoulmilk.receipt.domain.entity.ValidReceipt;
import com.seoulmilk.receipt.dto.request.ValidResponseSearchRequest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.Optional;

public interface ValidReceiptRepository {
    ValidReceipt save(ValidReceipt validReceipt);
    Optional<ValidReceipt> findById(String issueId);
    void deleteAll();

    Page<ValidReceipt> findAllBySpecification(
            ValidResponseSearchRequest validResponseSearchRequest, Pageable pageable
    );
}
