package com.seoulmilk.receipt.infrastructure.persistence.mapper;

import com.seoulmilk.receipt.domain.entity.ValidReceipt;
import com.seoulmilk.receipt.infrastructure.persistence.jpa.entity.ValidReceiptJpaEntity;
import org.springframework.stereotype.Component;

@Component
public class ValidReceiptMapper {
    public ValidReceipt toDomainEntity(ValidReceiptJpaEntity validReceiptJpaEntity) {
        return ValidReceipt.toDomainEntity(validReceiptJpaEntity);
    }

    public ValidReceiptJpaEntity toJpaEntity(ValidReceipt validReceipt) {
        return ValidReceiptJpaEntity.toJpaEntity(validReceipt);
    }
}
