package com.seoulmilk.invoice.application;

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

    public Mono<String> processFile(MultipartFile file) {
        return Mono.fromCallable(() -> {
                    try {
                        return extractFileProcessData(file);
                    } catch (IOException e) {
                        log.error("파일 변환 도중 에러가 발생했습니다.", e);
                        throw InvoiceErrorCode.FAILED_TO_PROCESS_FILE.toException();
                    }
                })
                .subscribeOn(Schedulers.boundedElastic())
                .flatMap(data -> processImg(data.fileMetaData, data.fileBytes, file.getName()))
                .map(OcrResponse::toString)
                .doOnError(e -> {
                    log.error("파일 변환 도중 에러가 발생했습니다.", e);
                    throw InvoiceErrorCode.FAILED_TO_PROCESS_FILE.toException();
                });
    }

    private FileProcessData extractFileProcessData(MultipartFile file) throws IOException {
        validateFilePresence(file);
        FileMetaData fileMetaData = createFileMetaData(file);
        byte[] fileBytes = file.getBytes();
        return new FileProcessData(fileMetaData, fileBytes);

    }

    public Mono<OcrResponse> processImg(FileMetaData fileMetaData, byte[] fileBytes, String fileName) {
        return executeOcr(fileMetaData, fileBytes, fileName)
                .doOnSuccess(this::publishOcrEvent);
    }

    private void publishOcrEvent(OcrResponse ocrResponse) {
        try {
            OcrValidationRequest ocrValidationRequest = OcrResponseConverter.convert(ocrResponse);
            ocrEventPublisher.publish(ocrValidationRequest);
        } catch (Exception e) {
            throw EventErrorCode.FAILED_TO_PUBLISH_EVENT.toException();
        }
    }

    private Mono<OcrResponse> executeOcr(FileMetaData fileMetaData, byte[] fileBytes, String fileName) {
        String requestMessage = createRequestMessage(fileMetaData);
        return webClientOcrEngine.extractText(requestMessage, fileBytes, fileName);
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

    private record FileProcessData(FileMetaData fileMetaData, byte[] fileBytes) {
    }
}
