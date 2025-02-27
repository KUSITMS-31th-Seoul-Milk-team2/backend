package com.seoulmilk.receipt.infrastructure.webclient;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.seoulmilk.receipt.exception.ReceiptErrorCode;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.WebClient;

import java.net.URLDecoder;
import java.nio.charset.StandardCharsets;
import java.util.Map;

@Component
@RequiredArgsConstructor
@Log4j2
public class TaxReceiptWebClientUtilImpl implements TaxReceiptWebClientUtil {
    private final ObjectMapper objectMapper;
    private final WebClient.Builder webClientBuilder;

    @Override
    public String post(String url, Map<String, String> headers, Object body) {
        return sendRequest(HttpMethod.POST, url, headers, body);
    }

    @Override
    public String get(String url, Map<String, String> headers) {
        return sendRequest(HttpMethod.GET, url, headers, null);
    }

    /**
     * WebClient를 사용하여 외부 서버에 요청을 보냅니다.
     * @param method
     * @param url
     * @param headers
     * @param body
     * @return WebClient 응답 결과
     */
    private  String sendRequest(HttpMethod method, String url, Map<String, String> headers, Object body) {
        WebClient.RequestBodySpec requestBodySpec = webClientBuilder
                .baseUrl(url)
                .build()
                .method(method)
                .headers(httpHeaders -> headers.forEach(httpHeaders::set));

        if (body instanceof String) {
            requestBodySpec.contentType(MediaType.APPLICATION_FORM_URLENCODED).bodyValue(body);
        } else {
            requestBodySpec.contentType(MediaType.APPLICATION_JSON).bodyValue(body);
        }

        return requestBodySpec.retrieve()
                .bodyToMono(String.class)
                .block();
    }

    /**
     * 응답 결과가 URLEncoding 되어 있는 경우 결과값을 디코딩 합니다.
     * @param response
     * @param methodName
     * @return
     */

    @Override
    public Map<String, Object> decodeResponse(String response, String methodName) {
        try {
            String decodedResponse = URLDecoder.decode(response, StandardCharsets.UTF_8);
            log.info("[{}] 받은 데이터 - {}", methodName, decodedResponse);
            return objectMapper.readValue(decodedResponse, Map.class);
        } catch (JsonProcessingException e) {
            log.error("[{}] 응답 JSON 역직렬화 실패", methodName, e);
            throw ReceiptErrorCode.JSON_DESERIALIZED_ERROR.toException();
        } catch (Exception e) {
            log.error("[{}] 요청 처리 중 오류 발생", methodName, e);
            throw ReceiptErrorCode.INVALID_FORMAT_ERROR.toException();
        }
    }

}
