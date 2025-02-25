package com.seoulmilk.invoice.application;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.seoulmilk.invoice.dto.request.OcrRequest;
import com.seoulmilk.invoice.dto.response.OcrResponse;
import com.seoulmilk.invoice.exception.InvoiceErrorCode;
import com.seoulmilk.invoice.infrastructure.configuration.properties.OcrProperties;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@Log4j2
@Service
@RequiredArgsConstructor
public class OpenFeignService {
    private final OpenFeignClient openFeignClient;
    private final OcrProperties ocrProperties;
    private final ObjectMapper objectMapper;

    public OcrResponse processImg(MultipartFile file) {
        String message = createMessage(file.getOriginalFilename());
        return openFeignClient.extractText(message, file);
    }

    private String createMessage(String filename) {
        try {
            OcrRequest request = new OcrRequest(
                    ocrProperties.getVersion(),
                    ocrProperties.getRequestId(),
                    System.currentTimeMillis(),
                    ocrProperties.getLang(),
                    List.of(new OcrRequest.ImageInfo(
                            getFileExtension(filename),
                            removeExtension(filename)
                    ))
            );

            return objectMapper.writeValueAsString(request);
        } catch (Exception e) {
            throw InvoiceErrorCode.FAILED_TO_CREATE_JSON.toException();
        }
    }


    private String getFileExtension(String filename) {
        int lastDotIndex = filename.lastIndexOf('.');
        if (lastDotIndex > 0) {
            return filename.substring(lastDotIndex + 1).toLowerCase();
        }
        return ocrProperties.getDefaultFormat();
    }

    private String removeExtension(String filename) {
        int lastDotIndex = filename.lastIndexOf('.');
        if (lastDotIndex > 0) {
            return filename.substring(0, lastDotIndex);
        }
        return filename;
    }
}
