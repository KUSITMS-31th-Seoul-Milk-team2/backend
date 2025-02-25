package com.seoulmilk.core.configuration.webclient;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpMethod;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.WebClient;

@Component
@RequiredArgsConstructor
public class WebClientUtil {
    private final WebClientConfig webClientConfig;

    // 기본 GET 메서드를 사용하는 WebClient 코드
    public WebClient.RequestBodySpec get(String url) {
        return webClientConfig.webClientBuilder()
                .method(HttpMethod.GET)
                .uri(url);
    }

    // 기본 POST 메서드를 사용하는 WebClient 코드
    public WebClient.RequestBodySpec post(String url) {
        return webClientConfig.webClientBuilder()
                .method(HttpMethod.POST)
                .uri(url);
    }
}
