package com.seoulmilk.receipt.domain;

import com.seoulmilk.receipt.domain.entity.ValidReceipt;

import java.util.Optional;

public interface ValidReceiptRepository {
    ValidReceipt save(ValidReceipt validReceipt);
    Optional<ValidReceipt> findById(String issueId);
    void deleteAll();
}
