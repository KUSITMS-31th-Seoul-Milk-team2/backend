package com.seoulmilk.receipt.infrastructure.persistence.repository;

import com.seoulmilk.receipt.infrastructure.persistence.jpa.entity.ReceiptJpaEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface ReceiptJpaRepository extends JpaRepository<ReceiptJpaEntity, Long> {
    Optional<ReceiptJpaEntity> findByIssueId(String issueId);
}
