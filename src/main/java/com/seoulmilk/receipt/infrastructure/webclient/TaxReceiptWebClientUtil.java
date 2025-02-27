package com.seoulmilk.receipt.infrastructure.webclient;

import org.springframework.http.HttpMethod;

import java.util.Map;

public interface TaxReceiptWebClientUtil {
    String post(String url, Map<String, String> headers, Object body);
    String get(String url, Map<String, String> headers);

    Map<String, Object> decodeResponse(String response, String methodName);
}
