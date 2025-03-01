package com.seoulmilk.invoice.application;

import com.seoulmilk.invoice.domain.factory.OcrRequestFactory;
import com.seoulmilk.invoice.domain.service.OcrEngine;
import com.seoulmilk.invoice.domain.value.FileMetaData;
import com.seoulmilk.invoice.dto.response.OcrResponse;
import com.seoulmilk.invoice.exception.InvoiceErrorCode;
import com.seoulmilk.invoice.infrastructure.converter.OcrRequestConverter;
import com.seoulmilk.invoice.infrastructure.converter.OcrResponseConverter;
import com.seoulmilk.receipt.dto.request.OcrValidationRequest;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

@Log4j2
@Service
@RequiredArgsConstructor
public class OpenFeignService {
    private final OcrEngine ocrEngine;
    private final OcrRequestConverter requestConverter;
    private final OcrRequestFactory requestFactory;
    private final OcrEventPublisher ocrEventPublisher;

    public OcrResponse processImg(MultipartFile file) {
        validateFilePresence(file);
        FileMetaData fileMetaData = createFileMetaData(file);
        OcrResponse ocrResponse = executeOcr(fileMetaData, file);
        publishOcrEvent(ocrResponse);
        return ocrResponse;
    }

    private void publishOcrEvent(OcrResponse ocrResponse) {
        OcrValidationRequest ocrValidationRequest = OcrResponseConverter.convert(ocrResponse);
        ocrEventPublisher.publish(ocrValidationRequest);
    }

    private OcrResponse executeOcr(FileMetaData fileMetaData, MultipartFile file) {
        String requestMessage = createRequestMessage(fileMetaData);
        return ocrEngine.extractText(requestMessage, file);
    }

    private FileMetaData createFileMetaData(MultipartFile file) {
        return new FileMetaData(
                file.getOriginalFilename(),
                file.getContentType()
        );
    }

    private void validateFilePresence(MultipartFile file) {
        if (file.isEmpty()) {
            throw InvoiceErrorCode.EMPTY_FILE.toException();
        }
    }

    private String createRequestMessage(FileMetaData metaData) {
        return requestConverter.toJson(requestFactory.create(metaData));
    }
}
