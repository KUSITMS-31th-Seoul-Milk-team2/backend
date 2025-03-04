package com.seoulmilk.receipt.infrastructure.repository;

import com.seoulmilk.receipt.domain.InValidReceiptRepository;
import com.seoulmilk.receipt.domain.entity.InValidReceipt;
import com.seoulmilk.receipt.exception.ReceiptErrorCode;
import com.seoulmilk.receipt.infrastructure.persistence.jpa.entity.InValidReceiptJpaEntity;
import com.seoulmilk.receipt.infrastructure.persistence.mapper.InValidReceiptMapper;
import com.seoulmilk.receipt.infrastructure.persistence.repository.InValidJpaReceiptRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
@RequiredArgsConstructor
@Log4j2
public class InValidReceiptRepositoryImpl implements InValidReceiptRepository {
    private final InValidReceiptMapper invalidReceiptMapper;
    private final InValidJpaReceiptRepository inValidJpaReceiptRepository;

    @Override
    public InValidReceipt save(InValidReceipt inValidReceipt) {
        InValidReceiptJpaEntity inValidReceiptJpaEntity = invalidReceiptMapper.toJpaEntity(inValidReceipt);
        if(inValidReceiptJpaEntity == null){
            throw ReceiptErrorCode.FAILED_TO_SAVE_RECEIPT.toException();
        }
        inValidJpaReceiptRepository.save(inValidReceiptJpaEntity);
        return invalidReceiptMapper.toDomainEntity(inValidReceiptJpaEntity);
    }

    @Override
    public Optional<InValidReceipt> findById(String issueId) {
        return inValidJpaReceiptRepository.findByIssueId(issueId).map(invalidReceiptMapper::toDomainEntity);
    }

    @Override
    public void deleteAll() {
        inValidJpaReceiptRepository.deleteAll();
    }
}
