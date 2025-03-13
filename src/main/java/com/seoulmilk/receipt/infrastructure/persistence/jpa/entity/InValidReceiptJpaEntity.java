package com.seoulmilk.receipt.infrastructure.persistence.jpa.entity;

import com.seoulmilk.core.infrastructure.jpa.entity.BaseLongIdEntity;
import com.seoulmilk.receipt.domain.entity.InValidReceipt;
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
@Table(name = "NTS_TAX_INVALID")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class InValidReceiptJpaEntity extends BaseLongIdEntity {
    @Column(nullable = false)
    private String employeeId;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 2)
    private Arap arap;

    @Column(nullable = false, length = 24)
    private String issueId;

    @Column(nullable = false, length = 8)
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

    @Column(length = 512)
    private String fileUrl;

    public static InValidReceiptJpaEntity toJpaEntity(InValidReceipt inValidReceipt) {
        return InValidReceiptJpaEntity.builder()
                .id(inValidReceipt.getId())
                .employeeId(inValidReceipt.getEmployeeId())
                .arap(inValidReceipt.getArap())
                .issueId(inValidReceipt.getIssueId())
                .issueDate(inValidReceipt.getIssueDate())
                .suId(inValidReceipt.getSuId())
                .suName(inValidReceipt.getSuName())
                .ipId(inValidReceipt.getIpId())
                .ipName(inValidReceipt.getIpName())
                .chargeTotal(inValidReceipt.getChargeTotal())
                .taxTotal(inValidReceipt.getTaxTotal())
                .grandTotal(inValidReceipt.getGrandTotal())
                .erdat(inValidReceipt.getErdat())
                .erzet(inValidReceipt.getErzet())
                .fileUrl(inValidReceipt.getFileUrl())
                .createdAt(inValidReceipt.getCreatedAt())
                .updatedAt(inValidReceipt.getUpdatedAt())
                .deleted(Optional.ofNullable(inValidReceipt.getDeleted()).orElse(false))
                .build();
    }
}
