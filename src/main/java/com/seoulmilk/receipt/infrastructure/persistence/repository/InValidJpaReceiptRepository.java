package com.seoulmilk.receipt.infrastructure.persistence.repository;

import com.seoulmilk.receipt.infrastructure.persistence.jpa.entity.InValidReceiptJpaEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface InValidJpaReceiptRepository extends JpaRepository<InValidReceiptJpaEntity, Long> {
    Optional<InValidReceiptJpaEntity> findByIssueId(String issueId);
}
