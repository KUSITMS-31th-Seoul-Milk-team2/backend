package com.seoulmilk.receipt.infrastructure.mono;

import reactor.core.publisher.Mono;

import java.util.Map;

public interface WebClientMonoUtil {
    Mono<String> post(String url, Map<String, String> headers, Object body);
    Mono<String> get(String url, Map<String, String> headers);

    Map<String, Object> decodeResponse(String response, String methodName);
}
