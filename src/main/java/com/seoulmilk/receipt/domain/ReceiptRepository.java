package com.seoulmilk.receipt.domain;

import com.seoulmilk.receipt.domain.entity.Receipt;

import java.util.Optional;

public interface ReceiptRepository {
    Receipt save(Receipt receipt);
    Optional<Receipt> findById(Long id);
    void deleteAll();
}
