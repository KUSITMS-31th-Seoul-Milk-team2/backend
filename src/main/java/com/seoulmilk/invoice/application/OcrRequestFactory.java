package com.seoulmilk.invoice.application;

import com.seoulmilk.invoice.domain.value.FileMetaData;
import com.seoulmilk.invoice.dto.request.OcrRequest;
import com.seoulmilk.invoice.infrastructure.properties.OcrProperties;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.List;

@RequiredArgsConstructor
@Component
public class OcrRequestFactory {
    private final OcrProperties ocrProperties;

    public OcrRequest create(FileMetaData fileMetaData) {
        return new OcrRequest(
                ocrProperties.getVersion(),
                ocrProperties.getRequestId(),
                System.currentTimeMillis(),
                ocrProperties.getLang(),
                List.of(new OcrRequest.ImageInfo(
                        fileMetaData.getExtension(fileMetaData.filename()),
                        fileMetaData.getBaseFilename(fileMetaData.filename())
                ))
        );
    }
}
