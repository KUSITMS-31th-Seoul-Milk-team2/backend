package com.seoulmilk.invoice;

import com.seoulmilk.core.util.fileUtil.FileUtil;
import com.seoulmilk.invoice.application.OpenFeignService;
import com.seoulmilk.invoice.application.exception.InvoiceErrorCode;
import com.seoulmilk.invoice.domain.factory.OcrRequestFactory;
import com.seoulmilk.invoice.domain.value.FileMetaData;
import com.seoulmilk.invoice.dto.request.OcrRequest;
import com.seoulmilk.invoice.dto.response.OcrResponse;
import com.seoulmilk.invoice.infrastructure.OpenFeignClient;
import com.seoulmilk.invoice.infrastructure.converter.OcrRequestConverter;
import com.seoulmilk.receipt.dto.request.OcrValidationRequest;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

import static org.assertj.core.api.AssertionsForClassTypes.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class OpenFeignServiceTest {
    @Mock
    private OpenFeignClient openFeignClient;

    @Mock
    private OcrRequestConverter requestConverter;

    @Mock
    private OcrRequestFactory requestFactory;

    @Mock
    private FileUtil fileUtil;

    @Mock
    private MultipartFile file;

    @InjectMocks
    private OpenFeignService openFeignService;

    private static final Long EMPLOYEE_PK = 123L;

    private String fileUrl;
    private String originalFilename;
    private String contentType;
    private String requestMessage;
    private OcrResponse ocrResponse;
    private FileMetaData metaData;
    private OcrRequest ocrRequest;

    @BeforeEach
    void setUp() {
        fileUrl = "https://example.com/file.jpg";
        originalFilename = "file.jpg";
        contentType = "image/jpeg";
        requestMessage = "{ \"request\": \"data\" }";
        metaData = new FileMetaData(originalFilename, contentType);
        ocrRequest = new OcrRequest(
                "1",
                "1",
                0,
                "ko",
                List.of(new OcrRequest.ImageInfo("format", "name"))
        );
        ocrResponse = new OcrResponse(
                "1",
                "1",
                0,
                List.of(new OcrResponse.ImageResult(
                        List.of(
                                new OcrResponse.ImageResult.Field("공급자 사업자등록번호", "123-45-67890"),  // [10자리]
                                new OcrResponse.ImageResult.Field("공급받는자 사업자등록번호", "9876543210987"), // [13자리]
                                new OcrResponse.ImageResult.Field("총 세액 합계", "12,345원"),
                                new OcrResponse.ImageResult.Field("총액", "￦78,901"),
                                new OcrResponse.ImageResult.Field("승인번호", "2025-03-11-ABCDE"),
                                new OcrResponse.ImageResult.Field("전자세금계산서 작성일자", "2025-03-11"),
                                new OcrResponse.ImageResult.Field("총 공급가액", "1,234,567원"),
                                new OcrResponse.ImageResult.Field("공급자 사업체명", "(주)서울우유"),
                                new OcrResponse.ImageResult.Field("공급받는자 사업체명", "멋진컴퍼니")
                        ),
                        new OcrResponse.ImageResult.Title("name", "TestOcr")
                ))
        );
    }

    @Test
    @DisplayName("올바른 파일이 주어지면 Ocr 추출에 성공한다.")
    void processImg_validFile_success() {
        // Given
        when(file.isEmpty()).thenReturn(false);
        when(file.getOriginalFilename()).thenReturn(originalFilename);
        when(file.getContentType()).thenReturn(contentType);
        when(fileUtil.uploadFile(file)).thenReturn(fileUrl);
        when(requestFactory.create(metaData)).thenReturn(ocrRequest);
        when(requestConverter.toJson(ocrRequest)).thenReturn(requestMessage);
        when(openFeignClient.extractText(requestMessage, file)).thenReturn(ocrResponse);


        // When
        OcrValidationRequest result = openFeignService.processImg(EMPLOYEE_PK, file);

        // Then
        assertNotNull(result);
        verify(fileUtil).uploadFile(file);
        verify(requestFactory).create(metaData);
        verify(requestConverter).toJson(ocrRequest);
        verify(openFeignClient).extractText(requestMessage, file);
    }

    @Test
    @DisplayName("빈 파일이 주어지면 InvoiceErrorCode.EMPTY_FILE 를 던진다.")
    void processImg_emptyFile_exception() {
        // Given
        when(file.isEmpty()).thenReturn(true);

        // When & Then
        assertThatThrownBy(() -> openFeignService.processImg(EMPLOYEE_PK, file))
                .isInstanceOf(InvoiceErrorCode.EMPTY_FILE.toException().getClass());
    }
}
