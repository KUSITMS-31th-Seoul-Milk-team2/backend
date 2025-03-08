package com.seoulmilk.invoice.infrastructure.configuration;

import feign.RequestInterceptor;
import feign.form.spring.SpringFormEncoder;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.http.HttpMessageConverters;
import org.springframework.cloud.openfeign.support.SpringEncoder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.converter.ByteArrayHttpMessageConverter;
import org.springframework.http.converter.ResourceHttpMessageConverter;

@Configuration
public class OpenFeignConfiguration {

    @Bean
    public RequestInterceptor apiKeyInterceptor(
            @Value("${clova.ocr.secret-key}") String secretKey
    ) {
        return template -> template.header("X-OCR-SECRET", secretKey);
    }

    @Bean
    public SpringFormEncoder feignFormEncoder() {
        return new SpringFormEncoder(new SpringEncoder(
                () -> new HttpMessageConverters(
                        new ByteArrayHttpMessageConverter(),
                        new ResourceHttpMessageConverter()
                )
        ));
    }
}
