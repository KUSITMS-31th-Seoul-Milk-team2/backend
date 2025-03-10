package com.seoulmilk.receipt.infrastructure;

import com.seoulmilk.receipt.domain.entity.ValidReceipt;
import com.seoulmilk.receipt.exception.ExcelErrorCode;
import lombok.RequiredArgsConstructor;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.stereotype.Component;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.List;

@Component
@RequiredArgsConstructor
public class ExcelGenerator {

    private static final String SHEET_NAME = "NTS";
    private static final String[] HEADERS = {"ID", "사원번호", "매출매입구분", "승인번호", "서명일자", "공급자 사업자등록번호", "공급자 사업체명",
            "공급받는자 사업자등록번호", "공급받는자 사업체명", "총 공급가액 합계", "총 세액 합계", "총액(공급가액+세액)", "생성일",
            "생성시간", "File URL"};

    public byte[] generateExcel(List<ValidReceipt> validReceipts) {
        try (XSSFWorkbook workbook = new XSSFWorkbook();
             ByteArrayOutputStream outputStream = new ByteArrayOutputStream()) {

            XSSFSheet sheet = workbook.createSheet(SHEET_NAME);
            createHeaderRow(sheet);
            fillDataRows(sheet, validReceipts);
            autoSizeColumns(sheet);

            workbook.write(outputStream);
            return outputStream.toByteArray();
        } catch (IOException e) {
            throw ExcelErrorCode.FAIL_TO_DOWNLOAD.toException();
        }
    }

    private void createHeaderRow(XSSFSheet sheet) {
        Row headerRow = sheet.createRow(0);
        for (int i = 0; i < HEADERS.length; i++) {
            Cell cell = headerRow.createCell(i);
            cell.setCellValue(HEADERS[i]);
        }
    }

    private void fillDataRows(XSSFSheet sheet, List<ValidReceipt> validReceipts) {
        int rowNum = 1;
        for (ValidReceipt receipt : validReceipts) {
            Row row = sheet.createRow(rowNum++);
            fillReceiptData(row, receipt);
        }
    }

    private void fillReceiptData(Row row, ValidReceipt receipt) {
        row.createCell(0).setCellValue(receipt.getId());
        row.createCell(1).setCellValue(receipt.getEmployeeId());
        row.createCell(2).setCellValue(receipt.getArap().toString());
        row.createCell(3).setCellValue(receipt.getIssueId());
        row.createCell(4).setCellValue(receipt.getIssueDate());
        row.createCell(5).setCellValue(receipt.getSuId());
        row.createCell(6).setCellValue(receipt.getSuName());
        row.createCell(7).setCellValue(receipt.getIpId());
        row.createCell(8).setCellValue(receipt.getIpName());
        row.createCell(9).setCellValue(receipt.getChargeTotal());
        row.createCell(10).setCellValue(receipt.getTaxTotal());
        row.createCell(11).setCellValue(receipt.getGrandTotal());
        row.createCell(12).setCellValue(receipt.getErdat());
        row.createCell(13).setCellValue(receipt.getErzet());
        row.createCell(14).setCellValue(receipt.getFileUrl());
    }

    private void autoSizeColumns(XSSFSheet sheet) {
        for (int i = 0; i < HEADERS.length; i++) {
            sheet.autoSizeColumn(i);
        }
    }
}
