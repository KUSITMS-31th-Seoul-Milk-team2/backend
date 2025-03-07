package com.seoulmilk.receipt.infrastructure.repository;

import com.seoulmilk.receipt.domain.ValidReceiptRepository;
import com.seoulmilk.receipt.domain.entity.ValidReceipt;
import com.seoulmilk.receipt.dto.request.ValidResponseSearchRequest;
import com.seoulmilk.receipt.exception.ReceiptErrorCode;
import com.seoulmilk.receipt.infrastructure.persistence.jpa.entity.ValidReceiptJpaEntity;
import com.seoulmilk.receipt.infrastructure.persistence.mapper.ValidReceiptMapper;
import com.seoulmilk.receipt.infrastructure.persistence.repository.ValidReceiptJpaRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
@RequiredArgsConstructor
@Log4j2
public class ValidReceiptRepositoryImpl implements ValidReceiptRepository {
    private final ValidReceiptMapper validReceiptMapper;
    private final ValidReceiptJpaRepository validReceiptJpaRepository;

    @Override
    public ValidReceipt save(ValidReceipt validReceipt) {
        ValidReceiptJpaEntity validReceiptJpaEntity = validReceiptMapper.toJpaEntity(validReceipt);
        if(validReceiptJpaEntity == null) {
            throw ReceiptErrorCode.FAILED_TO_SAVE_RECEIPT.toException();
        }
        validReceiptJpaRepository.save(validReceiptJpaEntity);
        return validReceiptMapper.toDomainEntity(validReceiptJpaEntity);
    }

    @Override
    public Optional<ValidReceipt> findById(String issueId) {
        return validReceiptJpaRepository.findByIssueId(issueId).map(validReceiptMapper::toDomainEntity);
    }

    @Override
    public void deleteAll() {
        validReceiptJpaRepository.deleteAll();
    }

    @Override
    public Page<ValidReceipt> findAllBySpecification(
            ValidResponseSearchRequest validResponseSearchRequest, Pageable pageable
    ) {
       return validReceiptJpaRepository.findAll(
            ValidReceiptSpecification.search(
                    validResponseSearchRequest
            ),
               pageable
       ).map(validReceiptMapper::toDomainEntity);
    }



}
