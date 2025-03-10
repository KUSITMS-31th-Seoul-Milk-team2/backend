package com.seoulmilk.receipt.infrastructure.repository;

import com.seoulmilk.receipt.domain.InValidReceiptRepository;
import com.seoulmilk.receipt.domain.entity.InValidReceipt;
import com.seoulmilk.receipt.exception.ReceiptErrorCode;
import com.seoulmilk.receipt.infrastructure.persistence.jpa.entity.InValidReceiptJpaEntity;
import com.seoulmilk.receipt.infrastructure.persistence.mapper.InValidReceiptMapper;
import com.seoulmilk.receipt.infrastructure.persistence.repository.InValidJpaReceiptRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
@RequiredArgsConstructor
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

    @Override
    public void deleteByIds(List<Long> pk) {
         inValidJpaReceiptRepository.deleteAllByIdIn(pk);
    }
}
