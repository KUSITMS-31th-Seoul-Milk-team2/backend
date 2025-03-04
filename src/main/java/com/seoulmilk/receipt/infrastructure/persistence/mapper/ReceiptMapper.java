package com.seoulmilk.receipt.infrastructure.persistence.mapper;

import com.seoulmilk.receipt.domain.entity.Receipt;
import com.seoulmilk.receipt.infrastructure.persistence.jpa.entity.ReceiptJpaEntity;
import org.springframework.stereotype.Component;

@Component
public class ReceiptMapper {
    public Receipt toDomainEntity(ReceiptJpaEntity receiptJpaEntity) {
        return Receipt.toDomainEntity(receiptJpaEntity);
    }

    public ReceiptJpaEntity toJpaEntity(Receipt receipt) {
        return ReceiptJpaEntity.toJpaEntity(receipt);
    }
}
