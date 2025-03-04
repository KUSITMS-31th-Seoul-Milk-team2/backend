package com.seoulmilk.receipt.domain;

import com.seoulmilk.receipt.domain.entity.InValidReceipt;

import java.util.Optional;

public interface InValidReceiptRepository {
    InValidReceipt save(InValidReceipt validReceipt);
    Optional<InValidReceipt> findById(String issueId);
    void deleteAll();
}
