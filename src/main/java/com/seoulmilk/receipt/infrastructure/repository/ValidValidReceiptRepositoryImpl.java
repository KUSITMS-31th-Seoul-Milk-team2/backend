package com.seoulmilk.receipt.infrastructure.repository;

import com.seoulmilk.receipt.domain.ValidReceiptRepository;
import com.seoulmilk.receipt.domain.entity.ValidReceipt;
import com.seoulmilk.receipt.exception.ReceiptErrorCode;
import com.seoulmilk.receipt.infrastructure.persistence.jpa.entity.ValidReceiptJpaEntity;
import com.seoulmilk.receipt.infrastructure.persistence.mapper.ValidReceiptMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
@RequiredArgsConstructor
@Log4j2
public class ValidValidReceiptRepositoryImpl implements ValidReceiptRepository {
    private final ValidReceiptMapper validReceiptMapper;
    private final com.seoulmilk.receipt.infrastructure.persistence.repository.ValidReceiptRepository validReceiptRepository;


    @Override
    public ValidReceipt save(ValidReceipt validReceipt) {
        ValidReceiptJpaEntity validReceiptJpaEntity = validReceiptMapper.toJpaEntity(validReceipt);
        if(validReceiptJpaEntity == null) {
            throw ReceiptErrorCode.FAILED_TO_SAVE_RECEIPT.toException();
        }
        validReceiptRepository.save(validReceiptJpaEntity);
        return validReceiptMapper.toDomainEntity(validReceiptJpaEntity);
    }

    @Override
    public Optional<ValidReceipt> findById(String issueId) {
        return validReceiptRepository.findByIssueId(issueId).map(validReceiptMapper::toDomainEntity);
    }

    @Override
    public void deleteAll() {
        validReceiptRepository.deleteAll();
    }
}
