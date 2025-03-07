//package com.seoulmilk.invoice.application;
//
//import com.seoulmilk.batch.infra.OpenFeignClient;
//import com.seoulmilk.core.util.fileUtil.FileUtil;
//import com.seoulmilk.invoice.domain.factory.OcrRequestFactory;
//import com.seoulmilk.invoice.domain.value.FileMetaData;
//import com.seoulmilk.invoice.dto.response.OcrResponse;
//import com.seoulmilk.invoice.exception.InvoiceErrorCode;
//import com.seoulmilk.invoice.infrastructure.converter.OcrRequestConverter;
//import com.seoulmilk.invoice.infrastructure.converter.OcrResponseConverter;
//import com.seoulmilk.receipt.dto.request.OcrValidationRequest;
//import lombok.RequiredArgsConstructor;
//import lombok.extern.log4j.Log4j2;
//import org.springframework.stereotype.Service;
//import org.springframework.web.multipart.MultipartFile;
//
//@Log4j2
//@Service
//@RequiredArgsConstructor
//public class OpenFeignService {
//    private final OpenFeignClient openFeignClient;
//    private final OcrRequestConverter requestConverter;
//    private final OcrRequestFactory requestFactory;
//    private final FileUtil fileUtil;
//
//    public OcrValidationRequest processImg(Long empPk, MultipartFile file) {
//        validateFilePresence(file);
//        String fileUrl = fileUtil.uploadFile(file);
//        FileMetaData fileMetaData = createFileMetaData(file);
//        OcrResponse ocrResponse = executeOcr(fileMetaData, file);
//
//        return OcrResponseConverter.convert(empPk, fileUrl, ocrResponse);
//    }
//
//
//    private OcrResponse executeOcr(FileMetaData fileMetaData, MultipartFile file) {
//        String requestMessage = createRequestMessage(fileMetaData);
//        return openFeignClient.extractText(requestMessage, file);
//    }
//
//    private FileMetaData createFileMetaData(MultipartFile file) {
//        return new FileMetaData(
//                file.getOriginalFilename(),
//                file.getContentType()
//        );
//    }
//
//    private void validateFilePresence(MultipartFile file) {
//        if (file.isEmpty()) {
//            throw InvoiceErrorCode.EMPTY_FILE.toException();
//        }
//    }
//
//    private String createRequestMessage(FileMetaData metaData) {
//        return requestConverter.toJson(requestFactory.create(metaData));
//    }
//}
