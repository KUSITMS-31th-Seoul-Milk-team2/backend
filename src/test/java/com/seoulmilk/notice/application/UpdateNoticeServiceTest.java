package com.seoulmilk.notice.application;

import com.seoulmilk.core.infrastructure.security.CustomUserDetails;
import com.seoulmilk.core.util.fileUtil.FileUtil;
import com.seoulmilk.notice.domain.entity.Notice;
import com.seoulmilk.notice.domain.repository.NoticeRepository;
import com.seoulmilk.notice.dto.request.UpdateNoticeRequest;
import com.seoulmilk.notice.exception.NoticeErrorCode;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.web.multipart.MultipartFile;

import java.util.Optional;

import static org.assertj.core.api.AssertionsForClassTypes.assertThatThrownBy;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class UpdateNoticeServiceTest {
    @Mock
    private NoticeRepository noticeRepository;

    @Mock
    private FileUtil fileUtil;

    @InjectMocks
    private UpdateNoticeService updateNoticeService;

    private final String testFileUrl = "test.png";
    private final CustomUserDetails userDetails = mock(CustomUserDetails.class);
    private final UpdateNoticeRequest request = new UpdateNoticeRequest(1L, "제목", "내용");
    private final MultipartFile mockFile = new MockMultipartFile("file", "test.txt", "text/plain", "content".getBytes());

    @Test
    @DisplayName("공지사항 업데이트 성공")
    void updateNotice_success_title() {
        // Given
        Notice notice = Notice.builder().authorPk(1L).build();
        when(noticeRepository.findById(request.id())).thenReturn(Optional.of(notice));
        when(userDetails.getId()).thenReturn(1L);
        when(fileUtil.uploadFile(mockFile)).thenReturn(testFileUrl);

        // When
        updateNoticeService.updateNotice(userDetails, request, mockFile);

        // Then
         verify(noticeRepository).updateNotice(request, testFileUrl);
    }

    @Test
    @DisplayName("공지사항 업데이트 실패 : 작성자가 아닌 경우")
    void updateNotice_failed_notAuthor() {
        // Given
        Notice notice = Notice.builder().authorPk(2L).build();
        when(noticeRepository.findById(request.id())).thenReturn(Optional.of(notice));
        when(userDetails.getId()).thenReturn(1L);

        // When & Then
        assertThatThrownBy(() -> updateNoticeService.updateNotice(userDetails, request, mockFile))
                .isInstanceOf(NoticeErrorCode.NOT_AN_AUTHOR.toException().getClass())
                .hasMessageContaining("자신이 쓴 공지만 수정 또는 삭제가 가능합니다.");

        verify(noticeRepository, never()).updateNotice(request, testFileUrl);
    }
}
