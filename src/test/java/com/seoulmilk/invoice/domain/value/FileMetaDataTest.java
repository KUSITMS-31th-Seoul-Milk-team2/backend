package com.seoulmilk.invoice.domain.value;

import com.seoulmilk.core.exception.DomainException;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

import static com.seoulmilk.invoice.application.exception.InvoiceErrorCode.NOT_SUPPORTED_EXTENSION;
import static com.seoulmilk.invoice.application.exception.InvoiceErrorCode.NOT_SUPPORTED_FILE;
import static org.junit.jupiter.api.Assertions.*;

public class FileMetaDataTest {
    @Nested
    @DisplayName("확장자 검증 테스트")
    class ExtensionValidationTest {

        @ParameterizedTest
        @ValueSource(strings = {"file.jpg", "image.jpeg", "doc.pdf", "photo.png"})
        @DisplayName("허용된 확장자로 생성 성공")
        void validExtensions(String filename) {
            assertDoesNotThrow(() -> new FileMetaData(filename, "image/jpeg"));
        }

        @ParameterizedTest
        @ValueSource(strings = {"file.txt", "data.xlsx", "no_extension"})
        @DisplayName("비허용 확장자로 예외 발생")
        void invalidExtensions(String filename) {
            DomainException exception = assertThrows(DomainException.class, () -> new FileMetaData(filename, "image/jpeg"));
            assertEquals(NOT_SUPPORTED_EXTENSION.getHttpStatus(), exception.getHttpStatus());
        }
    }

    @Nested
    @DisplayName("MIME 타입 검증 테스트")
    class MimeTypeValidationTest {

        @ParameterizedTest
        @ValueSource(strings = {"image/jpeg", "image/png", "application/pdf"})
        @DisplayName("허용된 MIME 타입으로 생성 성공")
        void validMimeTypes(String mimeType) {
            assertDoesNotThrow(() -> new FileMetaData("test.jpg", mimeType));
        }

        @ParameterizedTest
        @ValueSource(strings = {"text/plain", "image/gif", "application/octet-stream"})
        @DisplayName("비허용 MIME 타입으로 예외 발생")
        void invalidMimeTypes(String mimeType) {
            DomainException exception = assertThrows(DomainException.class, () -> new FileMetaData("test.jpg", mimeType));
            assertEquals(NOT_SUPPORTED_FILE.getHttpStatus() , exception.getHttpStatus());
        }
    }

    @Nested
    @DisplayName("getBaseFilename & getExtension 테스트")
    class UtilityMethodTest {

        @Test
        @DisplayName("복합 확장자 처리")
        void complexFilename() {
            assertEquals("archive.tar", FileMetaData.getBaseFilename("archive.tar.gz"));
            assertEquals("gz", FileMetaData.getExtension("archive.tar.gz"));
        }

        @Test
        @DisplayName("대문자 확장자 자동 소문자 변환")
        void uppercaseExtension() {
            assertEquals("jpg", FileMetaData.getExtension("IMAGE.JPG"));
        }

        @Test
        @DisplayName("확장자 없음")
        void noExtension() {
            assertEquals("document", FileMetaData.getBaseFilename("document"));
            assertEquals("", FileMetaData.getExtension("document"));
        }
    }
}
