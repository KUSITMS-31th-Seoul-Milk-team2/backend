package com.seoulmilk.receipt.infrastructure.persistence.jpa.entity;

import com.seoulmilk.core.infrastructure.jpa.entity.BaseLongIdEntity;
import com.seoulmilk.receipt.domain.entity.Receipt;
import com.seoulmilk.receipt.domain.value.Arap;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.Table;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

import java.util.Optional;

@Entity
@Getter
@SuperBuilder
@Table(name = "NTS_TAX")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class ReceiptJpaEntity extends BaseLongIdEntity {
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

    private String fileUrl;

    public static ReceiptJpaEntity toJpaEntity(Receipt receipt) {
        return ReceiptJpaEntity.builder()
                .id(receipt.getId())
                .employeeId(receipt.getEmployeeId())
                .arap(receipt.getArap())
                .issueId(receipt.getIssueId())
                .issueDate(receipt.getIssueDate())
                .suId(receipt.getSuId())
                .suName(receipt.getSuName())
                .ipId(receipt.getIpId())
                .ipName(receipt.getIpName())
                .chargeTotal(receipt.getChargeTotal())
                .taxTotal(receipt.getTaxTotal())
                .grandTotal(receipt.getGrandTotal())
                .erdat(receipt.getErdat())
                .erzet(receipt.getErzet())
                .fileUrl(receipt.getFileUrl())
                .createdAt(receipt.getCreatedAt())
                .updatedAt(receipt.getUpdatedAt())
                .deleted(Optional.ofNullable(receipt.getDeleted()).orElse(false))
                .build();
    }
}
