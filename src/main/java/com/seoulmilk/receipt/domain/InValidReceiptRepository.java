package com.seoulmilk.receipt.domain;

import com.seoulmilk.receipt.domain.entity.InValidReceipt;
import com.seoulmilk.receipt.infrastructure.persistence.jpa.entity.InValidReceiptJpaEntity;
import jakarta.transaction.Transactional;

import java.util.List;
import java.util.Optional;

public interface InValidReceiptRepository {
    InValidReceipt save(InValidReceipt validReceipt);
    Optional<InValidReceipt> findById(Long pk);
    void deleteAll();
    void deleteById(Long pk);
    void deleteByIds(List<Long> pk);
}
