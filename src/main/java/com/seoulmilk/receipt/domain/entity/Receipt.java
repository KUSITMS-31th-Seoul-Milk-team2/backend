package com.seoulmilk.receipt.domain.entity;

import com.seoulmilk.receipt.domain.value.Arap;
import lombok.Builder;
import lombok.Getter;

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

}
