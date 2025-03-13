package com.seoulmilk.notice.application;

import com.seoulmilk.core.infrastructure.security.CustomUserDetails;
import com.seoulmilk.core.util.fileUtil.FileUtil;
import com.seoulmilk.notice.domain.entity.Notice;
import com.seoulmilk.notice.domain.repository.NoticeRepository;
import com.seoulmilk.notice.dto.request.PostNoticeRequest;
import com.seoulmilk.notice.dto.response.PostNoticeResponse;
import com.seoulmilk.notice.exception.NoticeErrorCode;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.web.multipart.MultipartFile;

import static org.assertj.core.api.AssertionsForClassTypes.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class PostNoticeServiceTest {
    @Mock
    private NoticeRepository noticeRepository;

    @Mock
    private FileUtil fileUtil;

    @InjectMocks
    private PostNoticeService postNoticeService;

    private final String testFileUrl = "test.png";
    private final CustomUserDetails userDetails = mock(CustomUserDetails.class);
    private final PostNoticeRequest request = new PostNoticeRequest("제목", "내용");
    private final MultipartFile mockFile = new MockMultipartFile("file", "test.txt", "text/plain", "content".getBytes());

    @Test
    @DisplayName("공지사항 등록 성공")
    void postNotice_success() {
        // Given
        when(userDetails.getId()).thenReturn(1L);
        when(userDetails.getUsername()).thenReturn("사원");
        when(fileUtil.uploadFile(any(MultipartFile.class))).thenReturn(testFileUrl);
        when(noticeRepository.save(any(Notice.class))).thenAnswer(invocation -> invocation.getArgument(0));

        // When
        PostNoticeResponse response = postNoticeService.post(userDetails, request, mockFile);

        // Then
        assertAll(
                () -> assertEquals(request.title(), response.title()),
                () -> assertEquals(request.content(), response.content()),
                () -> assertEquals(testFileUrl, response.fileUrl()),
                () -> verify(noticeRepository, times(1)).save(any(Notice.class)),
                () -> verify(fileUtil, times(1)).uploadFile(any(MultipartFile.class))
        );
    }

    @Test
    @DisplayName("공지사항 등록 실패")
    void postNotice_failed() {
        // Given
        when(fileUtil.uploadFile(any())).thenReturn(testFileUrl);
        doThrow(RuntimeException.class)
                .when(noticeRepository)
                .save(any(Notice.class));

        // When & Then
        assertThatThrownBy(() -> postNoticeService.post(userDetails, request, mockFile))
                .isInstanceOf(NoticeErrorCode.POST_NOTICE_FAILED.toException().getClass())
                .hasMessage(NoticeErrorCode.POST_NOTICE_FAILED.getMessage());
    }
}
