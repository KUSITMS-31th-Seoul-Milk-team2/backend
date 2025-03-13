package com.seoulmilk.receipt.infrastructure.persistence.jpa.entity;

import com.seoulmilk.core.infrastructure.jpa.entity.BaseLongIdEntity;
import com.seoulmilk.receipt.domain.entity.ValidReceipt;
import com.seoulmilk.receipt.domain.value.Arap;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

import java.util.Optional;

@Entity
@Getter
@SuperBuilder
@Table(name = "NTS_TAX_VALID")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class ValidReceiptJpaEntity extends BaseLongIdEntity {
    @Column(nullable = false, length = 20)
    private String employeeId;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 2)
    private Arap arap;

    @Column(nullable = false, length = 24)
    private String issueId;

    @Column(nullable = false, length = 10)
    private String issueDate;

    @Column(nullable = false, length = 13)
    private String suId;

    @Column(nullable = false, length = 40)
    private String suName;

    @Column(nullable = false, length = 13)
    private String ipId;

    @Column(nullable = false, length = 40)
    private String ipName;

    @Column(nullable = false)
    private Integer chargeTotal;

    @Column(nullable = false)
    private Integer taxTotal;

    @Column(nullable = false)
    private Integer grandTotal;

    @Column(nullable = false, length = 10)
    private String erdat;

    @Column(nullable = false, length = 10)
    private String erzet;

    @Column(nullable = false, length = 512)
    private String fileUrl;

    @Builder.Default
    private Boolean isShow = true;

    public static ValidReceiptJpaEntity toJpaEntity(ValidReceipt validReceipt) {
        return ValidReceiptJpaEntity.builder()
                .id(validReceipt.getId())
                .employeeId(validReceipt.getEmployeeId())
                .arap(validReceipt.getArap())
                .issueId(validReceipt.getIssueId())
                .issueDate(validReceipt.getIssueDate())
                .suId(validReceipt.getSuId())
                .suName(validReceipt.getSuName())
                .ipId(validReceipt.getIpId())
                .ipName(validReceipt.getIpName())
                .chargeTotal(validReceipt.getChargeTotal())
                .taxTotal(validReceipt.getTaxTotal())
                .grandTotal(validReceipt.getGrandTotal())
                .erdat(validReceipt.getErdat())
                .erzet(validReceipt.getErzet())
                .fileUrl(validReceipt.getFileUrl())
                .createdAt(validReceipt.getCreatedAt())
                .updatedAt(validReceipt.getUpdatedAt())
                .deleted(Optional.ofNullable(validReceipt.getDeleted()).orElse(false))
                .build();
    }
}
