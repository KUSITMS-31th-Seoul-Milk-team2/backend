package com.seoulmilk.receipt.infrastructure.persistence.repository;

import com.seoulmilk.receipt.infrastructure.persistence.jpa.entity.ValidReceiptJpaEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface ValidReceiptJpaRepository extends JpaRepository<ValidReceiptJpaEntity, Long> {
    Optional<ValidReceiptJpaEntity> findByIssueId(String issueId);
}
