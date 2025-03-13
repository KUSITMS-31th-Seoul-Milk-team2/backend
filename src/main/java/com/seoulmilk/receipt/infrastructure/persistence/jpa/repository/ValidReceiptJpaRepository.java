package com.seoulmilk.receipt.infrastructure.persistence.jpa.repository;

import com.seoulmilk.receipt.domain.entity.ValidReceipt;
import com.seoulmilk.receipt.dto.request.UpdateValidReceiptRequest;
import com.seoulmilk.receipt.infrastructure.persistence.jpa.entity.ValidReceiptJpaEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
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

    @Modifying(clearAutomatically = true, flushAutomatically = true)
    @Query("UPDATE ValidReceiptJpaEntity v SET v.isShow = false WHERE v.createdAt < :cutoff")
    int bulkUpdateExpiredReceipts(@Param("cutoff") LocalDateTime cutoff);

    @Query("SELECT v FROM ValidReceiptJpaEntity v WHERE v.isShow = true")
    List<ValidReceiptJpaEntity> findAllByIsShowTrue();

}
