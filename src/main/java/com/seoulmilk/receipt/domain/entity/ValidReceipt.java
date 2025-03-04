package com.seoulmilk.receipt.domain.entity;

import com.seoulmilk.receipt.domain.value.Arap;
import com.seoulmilk.receipt.infrastructure.persistence.jpa.entity.ValidReceiptJpaEntity;
import lombok.Builder;
import lombok.Getter;

import java.time.LocalDateTime;

@Getter
@Builder
public class ValidReceipt {
    private Long id;

    private String employeeId;

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

    private LocalDateTime createdAt;

    private LocalDateTime updatedAt;

    private Boolean deleted;

    public static ValidReceipt create(
            String employeeId, Arap arap, String issueId, String issueDate,
            String suId, String suName, String ipId, String ipName, Integer chargeTotal,
            Integer taxTotal, Integer grandTotal, String erdat, String erzet, String fileUrl
    ) {
        return ValidReceipt.builder()
                .id(null)
                .employeeId(employeeId)
                .arap(arap)
                .issueId(issueId)
                .issueDate(issueDate)
                .suId(suId)
                .suName(suName)
                .ipId(ipId)
                .ipName(ipName)
                .chargeTotal(chargeTotal)
                .taxTotal(taxTotal)
                .grandTotal(grandTotal)
                .erdat(erdat)
                .erzet(erzet)
                .fileUrl(fileUrl)
                .build();
    }

    public static ValidReceipt toDomainEntity(ValidReceiptJpaEntity validReceiptJpaEntity) {
        return ValidReceipt.builder()
                .id(validReceiptJpaEntity.getId())
                .employeeId(validReceiptJpaEntity.getEmployeeId())
                .arap(validReceiptJpaEntity.getArap())
                .issueId(validReceiptJpaEntity.getIssueId())
                .issueDate(validReceiptJpaEntity.getIssueDate())
                .suId(validReceiptJpaEntity.getSuId())
                .suName(validReceiptJpaEntity.getSuName())
                .ipId(validReceiptJpaEntity.getIpId())
                .ipName(validReceiptJpaEntity.getIpName())
                .chargeTotal(validReceiptJpaEntity.getChargeTotal())
                .taxTotal(validReceiptJpaEntity.getTaxTotal())
                .grandTotal(validReceiptJpaEntity.getGrandTotal())
                .erdat(validReceiptJpaEntity.getErdat())
                .erzet(validReceiptJpaEntity.getErzet())
                .fileUrl(validReceiptJpaEntity.getFileUrl())
                .createdAt(validReceiptJpaEntity.getCreatedAt())
                .updatedAt(validReceiptJpaEntity.getUpdatedAt())
                .deleted(validReceiptJpaEntity.getDeleted())
                .build();
    }
}
