package com.seoulmilk.receipt.domain;

import com.seoulmilk.receipt.domain.entity.InValidReceipt;
import jakarta.transaction.Transactional;

import java.util.List;
import java.util.Optional;

public interface InValidReceiptRepository {
    InValidReceipt save(InValidReceipt validReceipt);
    Optional<InValidReceipt> findById(String issueId);
    void deleteAll();
    void deleteByIds(List<Long> pk);
}
