package com.seoulmilk.receipt.infrastructure.factory;

import com.seoulmilk.emp.domain.entity.Emp;
import com.seoulmilk.receipt.domain.entity.InValidReceipt;
import com.seoulmilk.receipt.domain.entity.ValidReceipt;
import com.seoulmilk.receipt.domain.value.Arap;
import com.seoulmilk.receipt.dto.request.OcrValidationRequest;
import com.seoulmilk.receipt.dto.request.TaxReceiptValidationRequest;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class ReceiptFactory {
    public static ValidReceipt validReceiptCreate(Emp emp, OcrValidationRequest ocrValidationRequest){
        String erdat = getDateFormat("yyyy-MM-dd");
        String erzet = getDateFormat("HH:mm:ss");

        return ValidReceipt.create(
                emp.getEmployeeId(),
                Arap.AR,
                ocrValidationRequest.taxValidationInfo().approvalNo(),
                ocrValidationRequest.taxValidationInfo().reportingDate(),
                ocrValidationRequest.taxValidationInfo().supplierRegNumber(),
                ocrValidationRequest.taxValidationInfo().supplierName(),
                ocrValidationRequest.taxValidationInfo().contractorRegNumber(),
                ocrValidationRequest.taxValidationInfo().contractorName(),
                getChargeTotal(ocrValidationRequest.taxValidationInfo().grandTotal(), ocrValidationRequest.taxValidationInfo().taxTotal()),
                Integer.parseInt(ocrValidationRequest.taxValidationInfo().taxTotal()),
                Integer.parseInt(ocrValidationRequest.taxValidationInfo().grandTotal()),
                erdat,
                erzet,
                ocrValidationRequest.fileUrl()
        );
    }

    public static ValidReceipt validReceiptCreate(
            Emp emp,
            InValidReceipt inValidReceipt,
            TaxReceiptValidationRequest taxReceiptValidationRequest
    ){
        return ValidReceipt.create(
                emp.getEmployeeId(),
                inValidReceipt.getArap(),
                taxReceiptValidationRequest.approvalNo(),
                taxReceiptValidationRequest.reportingDate(),
                taxReceiptValidationRequest.supplierRegNumber(),
                inValidReceipt.getSuName(),
                taxReceiptValidationRequest.contractorRegNumber(),
                inValidReceipt.getIpName(),
                Integer.valueOf(taxReceiptValidationRequest.supplyValue()),
                inValidReceipt.getTaxTotal(),
                inValidReceipt.getGrandTotal(),
                inValidReceipt.getErdat(),
                inValidReceipt.getErzet(),
                inValidReceipt.getFileUrl()
        );
    }

    private static Integer getChargeTotal(Integer grandTotal, Integer taxTotal) {
        return grandTotal - taxTotal;
    }

    public static InValidReceipt inValidReceiptCreate(Emp emp, OcrValidationRequest ocrValidationRequest){
        String erdat = getDateFormat("yyyy-MM-dd");
        String erzet = getDateFormat("HH:mm:ss");

        return InValidReceipt.create(
                emp.getEmployeeId(),
                Arap.AR,
                ocrValidationRequest.taxValidationInfo().approvalNo(),
                ocrValidationRequest.taxValidationInfo().reportingDate(),
                ocrValidationRequest.taxValidationInfo().supplierRegNumber(),
                ocrValidationRequest.taxValidationInfo().supplierName(),
                ocrValidationRequest.taxValidationInfo().contractorRegNumber(),
                ocrValidationRequest.taxValidationInfo().contractorName(),
                Integer.parseInt(ocrValidationRequest.taxValidationInfo().supplyValue()),
                Integer.parseInt(ocrValidationRequest.taxValidationInfo().taxTotal()),
                Integer.parseInt(ocrValidationRequest.taxValidationInfo().grandTotal()),
                erdat,
                erzet,
                ocrValidationRequest.fileUrl()
        );
    }

    private static String getDateFormat(String pattern){
        return LocalDateTime.now().format(DateTimeFormatter.ofPattern(pattern));
    }

    private static Integer getChargeTotal(String grandTotal, String taxTotal) {
        return Integer.parseInt(grandTotal) - Integer.parseInt(taxTotal);
    }

}
