package com.seoulmilk.receipt.infrastructure.persistence.jpa.entity;

import com.seoulmilk.core.infrastructure.jpa.entity.BaseLongIdEntity;
import com.seoulmilk.receipt.domain.entity.ValidReceipt;
import com.seoulmilk.receipt.domain.value.Arap;
import jakarta.persistence.*;
import lombok.AccessLevel;
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
    private String employeeId;

    @Enumerated(EnumType.STRING)
    private Arap arap;

    private String issueId;

    private String issueDate;

    private String suId;

    private String suName;

    private String ipId;

    private String ipName;

    private Integer chargeTotal;

    private Integer taxTotal;

    private Integer grandTotal;

    private String erdat;

    private String erzet;

    @Column(length = 512)
    private String fileUrl;

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
