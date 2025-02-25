package com.seoulmilk.invoice.application;

import com.seoulmilk.core.configuration.openfeign.OpenFeignConfiguration;
import com.seoulmilk.invoice.dto.response.OcrResponse;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.multipart.MultipartFile;

@FeignClient(
        name = "openFeignClient",
        url = "${clova.ocr.ocr-invoke-url}",
        configuration = OpenFeignConfiguration.class
)
public interface OpenFeignClient {

    @PostMapping(
        consumes = MediaType.MULTIPART_FORM_DATA_VALUE
    )
    OcrResponse extractText(
            @RequestPart("message") String message,
            @RequestPart("file") MultipartFile file
    );
}
