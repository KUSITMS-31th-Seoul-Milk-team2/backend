package com.seoulmilk.notice.application;

import com.seoulmilk.core.infrastructure.security.CustomUserDetails;
import com.seoulmilk.notice.domain.entity.Notice;
import com.seoulmilk.notice.domain.repository.NoticeRepository;
import com.seoulmilk.notice.dto.request.DeleteNoticeRequest;
import com.seoulmilk.notice.exception.NoticeErrorCode;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

import static org.assertj.core.api.AssertionsForClassTypes.assertThatThrownBy;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class DeleteNoticeServiceTest {

    @Mock
    private NoticeRepository noticeRepository;

    @InjectMocks
    private DeleteNoticeService deleteNoticeService;

    private final CustomUserDetails userDetails = mock(CustomUserDetails.class);
    private final DeleteNoticeRequest validRequest = new DeleteNoticeRequest(List.of(1L, 2L));

    private List<Notice> createTestNotices(Long... authorIds) {
        return Arrays.stream(authorIds)
                .map(id -> Notice.builder().authorPk(id).build())
                .collect(Collectors.toList());
    }

    @Test
    @DisplayName("공지사항 삭제 성공 : 내가 쓴 공지사항")
    void deleteNotice_success() {
        // Given
        List<Notice> notices = createTestNotices(1L, 1L);
        when(noticeRepository.findAllByIds(validRequest.ids())).thenReturn(notices);
        when(userDetails.getId()).thenReturn(1L);

        // When
        deleteNoticeService.delete(userDetails, validRequest);

        // Then
        verify(noticeRepository).deleteAll(notices);
    }

    @Test
    @DisplayName("공지사항 삭제 실패 : 남이 쓴 공지사항")
    void deleteNotice_failed_notAuthor() {
        // Given
        List<Notice> notices = createTestNotices(1L, 2L);
        when(noticeRepository.findAllByIds(validRequest.ids())).thenReturn(notices);
        when(userDetails.getId()).thenReturn(1L);

        // When & Then
        assertThatThrownBy(() -> deleteNoticeService.delete(userDetails, validRequest))
                .isInstanceOf(NoticeErrorCode.NOT_AN_AUTHOR.toException().getClass())
                .hasMessageContaining("자신이 쓴 공지만 수정 또는 삭제가 가능합니다.");

        verify(noticeRepository, never()).deleteAll(any());
    }
}
