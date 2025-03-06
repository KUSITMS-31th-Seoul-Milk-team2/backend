package com.seoulmilk.receipt.infrastructure.factory;


import com.seoulmilk.emp.domain.entity.Emp;
import com.seoulmilk.invoice.dto.request.OcrRequest;
import com.seoulmilk.receipt.dto.request.OcrValidationRequest;
import com.seoulmilk.receipt.dto.request.TaxReceiptValidationRequest;

public class TaxReceiptValidationRequestFactory {
    public static TaxReceiptValidationRequest create(
            Emp emp,
            OcrValidationRequest ocrValidationRequest,
            String uuid
    ){
        return new TaxReceiptValidationRequest(
            "0004",
                "5",
                uuid,
                "1",
                emp.getName(),
                emp.getBirthday(),
                emp.getBirthday(),
                ocrValidationRequest.supplierRegNumber(),
                ocrValidationRequest.contractorRegNumber(),
                ocrValidationRequest.approvalNo(),
                ocrValidationRequest.reportingDate(),
                ocrValidationRequest.supplyValue(),
                emp.getTelecom()
        );
    }
}
