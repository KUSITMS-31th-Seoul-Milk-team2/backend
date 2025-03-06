package com.seoulmilk.invoice.application;

import com.seoulmilk.core.infrastructure.security.CustomUserDetails;
import com.seoulmilk.invoice.domain.factory.OcrRequestFactory;
import com.seoulmilk.invoice.domain.service.OcrEngine;
import com.seoulmilk.invoice.domain.value.FileMetaData;
import com.seoulmilk.invoice.dto.response.OcrResponse;
import com.seoulmilk.invoice.exception.EventErrorCode;
import com.seoulmilk.invoice.exception.InvoiceErrorCode;
import com.seoulmilk.invoice.infrastructure.converter.OcrRequestConverter;
import com.seoulmilk.invoice.infrastructure.converter.OcrResponseConverter;
import com.seoulmilk.receipt.dto.request.OcrValidationRequest;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import reactor.core.publisher.Mono;
import reactor.core.scheduler.Schedulers;

import java.io.IOException;

@Log4j2
@Service
@RequiredArgsConstructor
public class WebClientOcrService {
    private final OcrEngine webClientOcrEngine;
    private final OcrRequestConverter requestConverter;
    private final OcrRequestFactory requestFactory;
    private final OcrEventPublisher ocrEventPublisher;

    public Mono<String> processFile(CustomUserDetails customUserDetails, MultipartFile file) {
        return Mono.fromCallable(() -> {
                    try {
                        return extractFileProcessData(
                                customUserDetails.getId(),
                                file);
                    } catch (IOException e) {
                        log.error("파일 변환 도중 에러가 발생했습니다.", e);
                        throw InvoiceErrorCode.FAILED_TO_PROCESS_FILE.toException();
                    }
                })
                .subscribeOn(Schedulers.boundedElastic())
                .flatMap(data -> processImg(data.empPk, data.fileMetaData, data.fileBytes, file.getName()))
                .map(OcrResponse::toString)
                .doOnError(e -> {
                    log.error("파일 변환 도중 에러가 발생했습니다.", e);
                    throw InvoiceErrorCode.FAILED_TO_PROCESS_FILE.toException();
                });
    }

    private FileProcessData extractFileProcessData(Long empPk, MultipartFile file) throws IOException {
        validateFilePresence(file);
        FileMetaData fileMetaData = createFileMetaData(file);
        byte[] fileBytes = file.getBytes();
        return new FileProcessData(empPk, fileMetaData, fileBytes);
    }

    public Mono<OcrResponse> processImg(Long empPk, FileMetaData fileMetaData, byte[] fileBytes, String fileName) {
        return executeOcr(empPk, fileMetaData, fileBytes, fileName)
                .flatMap(ocrResponse ->
                        Mono.fromRunnable(() -> publishOcrEvent(empPk, ocrResponse))
                                .thenReturn(ocrResponse)
                );
    }

    private void publishOcrEvent(Long empPk, OcrResponse ocrResponse) {
        try {
            OcrValidationRequest ocrValidationRequest = OcrResponseConverter.convert(empPk, ocrResponse);
            ocrEventPublisher.publish(ocrValidationRequest);
        } catch (Exception e) {
            throw EventErrorCode.FAILED_TO_PUBLISH_EVENT.toException();
        }
    }

    private Mono<OcrResponse> executeOcr(Long empPk, FileMetaData fileMetaData, byte[] fileBytes, String fileName) {
        String requestMessage = createRequestMessage(fileMetaData);
        return webClientOcrEngine.extractText(empPk, requestMessage, fileBytes, fileName);
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

    private record FileProcessData(Long empPk, FileMetaData fileMetaData, byte[] fileBytes) {
    }
}
