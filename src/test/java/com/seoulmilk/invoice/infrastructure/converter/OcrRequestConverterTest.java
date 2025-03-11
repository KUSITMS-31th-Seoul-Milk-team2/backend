package com.seoulmilk.invoice.infrastructure.converter;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.seoulmilk.core.exception.DomainException;
import com.seoulmilk.invoice.application.exception.InvoiceErrorCode;
import com.seoulmilk.invoice.dto.request.OcrRequest;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class OcrRequestConverterTest {

    @Mock
    private ObjectMapper objectMapper = new ObjectMapper();

    @Test
    @DisplayName("toJson 메서드 테스트")
    void toJson_success() {
        // given
        OcrRequest ocrRequest = new OcrRequest(
                "1",
                "1",
                0,
                "ko",
                List.of(new OcrRequest.ImageInfo("format", "name"))
        );
        // when
        OcrRequestConverter converter = new OcrRequestConverter(new ObjectMapper());
        String result = converter.toJson(ocrRequest);
        // then
        assertEquals(result.getClass(), String.class);
    }

    @Test
    @DisplayName("toJson 메서드 변환 실패 시 예외 발생")
    void toJson_failed() throws JsonProcessingException {
        // given
        OcrRequest ocrRequest = new OcrRequest(
                "1",
                "1",
                0,
                "ko",
                List.of(new OcrRequest.ImageInfo("format", "name"))
        );

        when(objectMapper.writeValueAsString(ocrRequest))
                .thenThrow(new JsonProcessingException("JSON 변환 실패 예외 발생") {});

        OcrRequestConverter converter = new OcrRequestConverter(objectMapper);

        // when & then
        DomainException exception = assertThrows(
                DomainException.class,
                () -> converter.toJson(ocrRequest)
        );

        assertEquals(
                InvoiceErrorCode.FAILED_TO_CREATE_JSON.toException().getCode(),
                exception.getCode()
        );
        assertEquals("JSON 생성 중 에러가 발생했습니다.", exception.getMessage());
    }
}
