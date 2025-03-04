package com.seoulmilk.receipt.domain.entity;

import com.seoulmilk.receipt.domain.value.Arap;
import com.seoulmilk.receipt.infrastructure.persistence.jpa.entity.ReceiptJpaEntity;
import lombok.Builder;
import lombok.Getter;

import java.time.LocalDateTime;

@Getter
@Builder
public class Receipt {
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

    public static Receipt create(
            Long id, String employeeId, Arap arap, String issueId, String issueDate,
            String suId, String suName, String ipId, String ipName, Integer chargeTotal,
            Integer taxTotal, Integer grandTotal, String erdat, String erzet, String fileUrl
    ) {
        return Receipt.builder()
                .id(id)
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

    public static Receipt toDomainEntity(ReceiptJpaEntity receiptJpaEntity) {
        return Receipt.builder()
                .id(receiptJpaEntity.getId())
                .employeeId(receiptJpaEntity.getEmployeeId())
                .arap(receiptJpaEntity.getArap())
                .issueId(receiptJpaEntity.getIssueId())
                .issueDate(receiptJpaEntity.getIssueDate())
                .suId(receiptJpaEntity.getSuId())
                .suName(receiptJpaEntity.getSuName())
                .ipId(receiptJpaEntity.getIpId())
                .ipName(receiptJpaEntity.getIpName())
                .chargeTotal(receiptJpaEntity.getChargeTotal())
                .taxTotal(receiptJpaEntity.getTaxTotal())
                .grandTotal(receiptJpaEntity.getGrandTotal())
                .erdat(receiptJpaEntity.getErdat())
                .erzet(receiptJpaEntity.getErzet())
                .fileUrl(receiptJpaEntity.getFileUrl())
                .createdAt(receiptJpaEntity.getCreatedAt())
                .updatedAt(receiptJpaEntity.getUpdatedAt())
                .deleted(receiptJpaEntity.getDeleted())
                .build();
    }

}
