package com.seoulmilk.receipt.infrastructure.persistence.jpa.repository;

import com.seoulmilk.receipt.dto.request.UpdateValidReceiptRequest;
import com.seoulmilk.receipt.infrastructure.persistence.jpa.entity.ValidReceiptJpaEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

public interface ValidReceiptJpaRepository extends JpaRepository<ValidReceiptJpaEntity, Long>, JpaSpecificationExecutor<ValidReceiptJpaEntity> {
    Optional<ValidReceiptJpaEntity> findByIssueId(String issueId);

    @Modifying(clearAutomatically = true)
    @Query("UPDATE ValidReceiptJpaEntity v " +
            "SET v.chargeTotal = :#{#updateValidReceiptRequest.chargeTotal()}, " +
            "    v.taxTotal = :#{#updateValidReceiptRequest.taxTotal()}, " +
            "    v.grandTotal = :#{#updateValidReceiptRequest.grandTotal()}, " +
            "    v.erdat = :#{#updateValidReceiptRequest.erdat()}, " +
            "    v.erzet = :#{#updateValidReceiptRequest.erzet()} " +
            "WHERE v.id = :#{#updateValidReceiptRequest.id()}")
    int update(UpdateValidReceiptRequest updateValidReceiptRequest);
}
