package com.seoulmilk.receipt.infrastructure.persistence.repository;

import com.seoulmilk.emp.domain.entity.Emp;
import com.seoulmilk.receipt.domain.entity.ValidReceipt;
import com.seoulmilk.receipt.infrastructure.persistence.jpa.entity.ValidReceiptJpaEntity;
import jakarta.transaction.Transactional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Optional;

public interface ValidReceiptJpaRepository extends JpaRepository<ValidReceiptJpaEntity, Long>, JpaSpecificationExecutor<ValidReceiptJpaEntity> {
    Optional<ValidReceiptJpaEntity> findByIssueId(String issueId);
}
