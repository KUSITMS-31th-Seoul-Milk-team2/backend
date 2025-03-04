package com.seoulmilk.receipt.domain.entity;

import com.seoulmilk.receipt.domain.value.Arap;
import com.seoulmilk.receipt.infrastructure.persistence.jpa.entity.InValidReceiptJpaEntity;
import com.seoulmilk.receipt.infrastructure.persistence.jpa.entity.ValidReceiptJpaEntity;
import lombok.Builder;
import lombok.Getter;

import java.time.LocalDateTime;

@Getter
@Builder
public class InValidReceipt {
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

    public static InValidReceipt create(
            String employeeId, Arap arap, String issueId, String issueDate,
            String suId, String suName, String ipId, String ipName, Integer chargeTotal,
            Integer taxTotal, Integer grandTotal, String erdat, String erzet, String fileUrl
    ) {
        return InValidReceipt.builder()
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

    public static InValidReceipt toDomainEntity(InValidReceiptJpaEntity inValidReceiptJpaEntity) {
        return InValidReceipt.builder()
                .id(inValidReceiptJpaEntity.getId())
                .employeeId(inValidReceiptJpaEntity.getEmployeeId())
                .arap(inValidReceiptJpaEntity.getArap())
                .issueId(inValidReceiptJpaEntity.getIssueId())
                .issueDate(inValidReceiptJpaEntity.getIssueDate())
                .suId(inValidReceiptJpaEntity.getSuId())
                .suName(inValidReceiptJpaEntity.getSuName())
                .ipId(inValidReceiptJpaEntity.getIpId())
                .ipName(inValidReceiptJpaEntity.getIpName())
                .chargeTotal(inValidReceiptJpaEntity.getChargeTotal())
                .taxTotal(inValidReceiptJpaEntity.getTaxTotal())
                .grandTotal(inValidReceiptJpaEntity.getGrandTotal())
                .erdat(inValidReceiptJpaEntity.getErdat())
                .erzet(inValidReceiptJpaEntity.getErzet())
                .fileUrl(inValidReceiptJpaEntity.getFileUrl())
                .createdAt(inValidReceiptJpaEntity.getCreatedAt())
                .updatedAt(inValidReceiptJpaEntity.getUpdatedAt())
                .deleted(inValidReceiptJpaEntity.getDeleted())
                .build();
    }
}
