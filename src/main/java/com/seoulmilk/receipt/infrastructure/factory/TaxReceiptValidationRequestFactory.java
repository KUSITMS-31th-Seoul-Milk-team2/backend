package com.seoulmilk.receipt.infrastructure.factory;


import com.seoulmilk.emp.domain.entity.Emp;
import com.seoulmilk.receipt.dto.request.OcrValidationRequest;
import com.seoulmilk.receipt.dto.request.TaxReceiptValidationRequest;
import com.seoulmilk.receipt.presentation.dto.request.ValidationRequest;

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
                emp.getHometax().getValue(),
                emp.getName(),
                emp.getPhoneNumber(),
                emp.getBirthday(),
                ocrValidationRequest.taxValidationInfo().supplierRegNumber(),
                ocrValidationRequest.taxValidationInfo().contractorRegNumber(),
                ocrValidationRequest.taxValidationInfo().approvalNo(),
                ocrValidationRequest.taxValidationInfo().reportingDate(),
                ocrValidationRequest.taxValidationInfo().supplyValue(),
                emp.getTelecom().getTelecomNum()
        );
    }

    public static TaxReceiptValidationRequest create(
            Emp emp,
            ValidationRequest validationRequest,
            String uuid
    ){
        return new TaxReceiptValidationRequest(
                "0004",
                "5",
                uuid,
                emp.getHometax().getValue(),
                emp.getName(),
                emp.getPhoneNumber(),
                emp.getBirthday(),
                validationRequest.supplierRegNumber(),
                validationRequest.contractorRegNumber(),
                validationRequest.approvalNo(),
                validationRequest.reportingDate(),
                validationRequest.supplyValue(),
                emp.getTelecom().getTelecomNum()
        );
    }
}
