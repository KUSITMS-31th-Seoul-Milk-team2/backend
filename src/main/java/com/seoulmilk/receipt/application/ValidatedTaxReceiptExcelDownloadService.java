package com.seoulmilk.receipt.application;

import com.seoulmilk.receipt.domain.ValidReceiptRepository;
import com.seoulmilk.receipt.domain.entity.ValidReceipt;
import com.seoulmilk.receipt.infrastructure.ExcelGenerator;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class ValidatedTaxReceiptExcelDownloadService {

    private final ValidReceiptRepository validReceiptRepository;
    private final ExcelGenerator excelGenerator;

    public byte[] exportValidatedTaxReceiptToExcel() {
        List<ValidReceipt> validReceipts = validReceiptRepository.findAll();
        return excelGenerator.generateExcel(validReceipts);
    }
}
