package com.seoulmilk.receipt.infrastructure.repository;

import com.seoulmilk.receipt.domain.ReceiptRepository;
import com.seoulmilk.receipt.domain.entity.Receipt;
import com.seoulmilk.receipt.infrastructure.persistence.jpa.entity.ReceiptJpaEntity;
import com.seoulmilk.receipt.infrastructure.persistence.mapper.ReceiptMapper;
import com.seoulmilk.receipt.infrastructure.persistence.repository.ReceiptJpaRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
@RequiredArgsConstructor
@Log4j2
public class ReceiptRepositoryImpl implements ReceiptRepository {
    private final ReceiptMapper receiptMapper;
    private final ReceiptJpaRepository receiptJpaRepository;


    @Override
    public Receipt save(Receipt receipt) {
        ReceiptJpaEntity receiptJpaEntity = receiptMapper.toJpaEntity(receipt);
        if(receiptJpaEntity == null) {
            return null;
        }
        receiptJpaRepository.save(receiptJpaEntity);
        return receiptMapper.toDomainEntity(receiptJpaEntity);
    }

    @Override
    public Optional<Receipt> findById(String issueId) {
        return receiptJpaRepository.findByIssueId(issueId).map(receiptMapper::toDomainEntity);
    }

    @Override
    public void deleteAll() {
        receiptJpaRepository.deleteAll();
    }
}
