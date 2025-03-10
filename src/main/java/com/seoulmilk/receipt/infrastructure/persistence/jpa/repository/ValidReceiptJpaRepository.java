package com.seoulmilk.receipt.infrastructure.persistence.jpa.repository;

import com.seoulmilk.receipt.infrastructure.persistence.jpa.entity.ValidReceiptJpaEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

import java.util.Optional;

public interface ValidReceiptJpaRepository extends JpaRepository<ValidReceiptJpaEntity, Long>, JpaSpecificationExecutor<ValidReceiptJpaEntity> {
    Optional<ValidReceiptJpaEntity> findByIssueId(String issueId);
}
