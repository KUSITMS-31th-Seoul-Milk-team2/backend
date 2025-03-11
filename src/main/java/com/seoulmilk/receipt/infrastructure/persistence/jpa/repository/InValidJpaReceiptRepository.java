package com.seoulmilk.receipt.infrastructure.persistence.jpa.repository;

import com.seoulmilk.receipt.infrastructure.persistence.jpa.entity.InValidReceiptJpaEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface InValidJpaReceiptRepository extends JpaRepository<InValidReceiptJpaEntity, Long> {
    Optional<InValidReceiptJpaEntity> findByIssueId(String issueId);
    List<InValidReceiptJpaEntity> findAllByEmployeeId(String employeeId);
    void deleteAllByIdIn(List<Long> pk);
}
