package com.seoulmilk.receipt.infrastructure.persistence.mapper;

import com.seoulmilk.receipt.domain.entity.InValidReceipt;
import com.seoulmilk.receipt.domain.entity.ValidReceipt;
import com.seoulmilk.receipt.infrastructure.persistence.jpa.entity.InValidReceiptJpaEntity;
import com.seoulmilk.receipt.infrastructure.persistence.jpa.entity.ValidReceiptJpaEntity;
import org.springframework.stereotype.Component;

@Component
public class InValidReceiptMapper {
    public InValidReceipt toDomainEntity(InValidReceiptJpaEntity inValidReceiptJpaEntity) {
        return InValidReceipt.toDomainEntity(inValidReceiptJpaEntity);
    }

    public InValidReceiptJpaEntity toJpaEntity(InValidReceipt inValidReceipt) {
        return InValidReceiptJpaEntity.toJpaEntity(inValidReceipt);
    }
}
