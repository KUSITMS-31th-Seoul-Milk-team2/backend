package com.seoulmilk.core.util;

import com.seoulmilk.core.application.FileStorageService;
import com.seoulmilk.core.util.fileUtil.FileUtil;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.web.multipart.MultipartFile;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class FileUtilTest {
    @Mock
    private FileStorageService fileStorageService;

    @Mock
    private MultipartFile multipartFile;

    @InjectMocks
    private FileUtil fileUtil;

    @Test
    @DisplayName("파일 업로드 성공")
    void uploadFile_Success() throws Exception {
        // Given
        String fileUrl = "file.jpg";
        when(fileStorageService.uploadFile(any(MultipartFile.class))).thenReturn(fileUrl);

        // When
        String result = fileUtil.uploadFile(multipartFile);

        // Then
        assertEquals(fileUrl, result);
        verify(fileStorageService, times(1)).uploadFile(multipartFile);
    }

    @Test
    @DisplayName("파일이 null일 때")
    void uploadFile_NullFile() throws Exception {
        // When
        String result = fileUtil.uploadFile(null);

        // Then
        assertNull(result);
        verify(fileStorageService, never()).uploadFile(any());
    }
}
