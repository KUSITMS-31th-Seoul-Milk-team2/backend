package com.seoulmilk.notice.application;

import com.seoulmilk.core.application.CacheService;
import com.seoulmilk.emp.domain.entity.Emp;
import com.seoulmilk.emp.domain.repository.EmpRepository;
import com.seoulmilk.emp.exception.EmpErrorCode;
import com.seoulmilk.notice.domain.entity.Notice;
import com.seoulmilk.notice.domain.repository.NoticeRepository;
import com.seoulmilk.notice.dto.response.NoticeSummaryResponse;
import com.seoulmilk.notice.dto.response.PageResponse;
import com.seoulmilk.notice.dto.response.ReadNoticeResponse;
import com.seoulmilk.notice.exception.NoticeErrorCode;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class ReadNoticeServiceTest {

    @Mock
    private NoticeRepository noticeRepository;

    @Mock
    private EmpRepository empRepository;

    @Mock
    private CacheService cacheService;

    @InjectMocks
    private ReadNoticeService readNoticeService;

    private Notice createNotice(Long id, Long authorPk) {
        return Notice.builder()
                .id(id)
                .authorPk(authorPk)
                .title("테스트 제목")
                .content("테스트 내용")
                .createdAt(LocalDateTime.now())
                .createdAt(LocalDateTime.now())
                .build();
    }

    private Emp createEmp(Long id) {
        return Emp.builder()
                .id(id)
                .name("테스트 유저")
                .build();
    }

    @Nested
    @DisplayName("단일 공지사항 조회 테스트")
    class ReadOneNoticeTest {
        @Test
        @DisplayName("단일 공지사항 조회 - 정상 케이스")
        void readOneNotice_success() {
            // given
            Notice notice = createNotice(1L, 1L);
            when(noticeRepository.findById(1L)).thenReturn(Optional.of(notice));
            when(empRepository.findById(1L)).thenReturn(Optional.of(createEmp(1L)));

            // when
            ReadNoticeResponse readNoticeResponse = readNoticeService.readOneNotice(1L);

            // then
            assertThat(readNoticeResponse.title()).isEqualTo("테스트 제목");
            assertThat(readNoticeResponse.content()).isEqualTo("테스트 내용");
        }

        @Test
        @DisplayName("단일 공지사항 조회 - 존재하지 않는 공지")
        void readOneNotice_noticeNotFound() {
            // given
            when(noticeRepository.findById(anyLong())).thenReturn(Optional.empty());

            // when & then
            assertThatThrownBy(() -> readNoticeService.readOneNotice(1L))
                    .isInstanceOf(NoticeErrorCode.NOT_EXISTS_NOTICE.toException().getClass());
        }

        @Test
        @DisplayName("단일 공지사항 조회 - 작성자 정보 없음")
        void readOneNotice_authorNotFound() {
            // given
            Notice notice = createNotice(1L, 1L);
            when(noticeRepository.findById(1L)).thenReturn(Optional.of(notice));
            when(empRepository.findById(1L)).thenReturn(Optional.empty());

            // when & then
            assertThatThrownBy(() -> readNoticeService.readOneNotice(1L))
                    .isInstanceOf(EmpErrorCode.NOT_EXIST_EMPLOYEE.toException().getClass());
        }

        @Test
        @DisplayName("단일 공지사항 조회 - 내가 쓴 공지사항")
        void readOneNotice_myNotice() {
            // given
            Notice notice = createNotice(1L, 1L);
            when(noticeRepository.findById(1L)).thenReturn(Optional.of(notice));
            when(empRepository.findById(1L)).thenReturn(Optional.of(createEmp(1L)));

            // when
            ReadNoticeResponse readNoticeResponse = readNoticeService.readOneNotice(1L);

            // then
            assertThat(readNoticeResponse.title()).isEqualTo("테스트 제목");
            assertThat(readNoticeResponse.content()).isEqualTo("테스트 내용");
        }
    }

    @Nested
    @DisplayName("페이지별 공지사항 조회 테스트")
    class GetNoticesByPageTest {
        @Test
        @DisplayName("페이지별 공지사항 조회 - 정상 케이스")
        void getNoticesByPage_success() {
            // given
            Pageable pageable = PageRequest.of(0, 10);
            Notice notice = createNotice(1L, 1L);
            Page<Notice> noticePage = new PageImpl<>(List.of(notice));

            when(noticeRepository.findAllOrderByIdDesc(pageable)).thenReturn(noticePage);

            // when
            PageResponse<NoticeSummaryResponse> result = readNoticeService.getNoticesByPage(pageable);

            // then
            assertThat(result.content()).hasSize(1);
            assertThat(result.content().get(0).title()).isEqualTo("테스트 제목");
        }

        @Test
        @DisplayName("페이지별 공지사항 조회 - 공지사항 없음")
        void getNoticesByPage_noNotice() {
            // given
            Pageable pageable = PageRequest.of(0, 10);
            Page<Notice> noticePage = new PageImpl<>(List.of());

            when(noticeRepository.findAllOrderByIdDesc(pageable)).thenReturn(noticePage);

            // when
            PageResponse<NoticeSummaryResponse> result = readNoticeService.getNoticesByPage(pageable);

            // then
            assertThat(result.content()).isEmpty();
        }

        @Test
        @DisplayName("키워드 검색 - 복합 조건 테스트(검색조건 : 제목 + 내용)")
        void getNoticesByKeyword_complexCondition() {
            // Given
            Pageable pageable = PageRequest.of(0, 10);
            Notice notice = createNotice(1L, 1L);
            Page<Notice> noticePage = new PageImpl<>(List.of(notice));

            when(noticeRepository.findAllByKeyword(any(Specification.class), eq(pageable)))
                    .thenReturn(noticePage);

            // When
            PageResponse<NoticeSummaryResponse> result =
                    readNoticeService.getNoticesByKeyword("title_and_content", "테스트 제목", pageable);

            // Then
            assertThat(result.content()).hasSize(1);
            verify(noticeRepository).findAllByKeyword(any(Specification.class), eq(pageable));
        }

        @Test
        @DisplayName("키워드 검색 - 이름")
        void getNoticesByKeyword_author() {
            // Given
            Pageable pageable = PageRequest.of(0, 10);
            Notice notice = createNotice(1L, 1L);
            Page<Notice> noticePage = new PageImpl<>(List.of(notice));

            when(noticeRepository.findAllByKeyword(any(Specification.class), eq(pageable)))
                    .thenReturn(noticePage);

            // When
            PageResponse<NoticeSummaryResponse> result =
                    readNoticeService.getNoticesByKeyword("author", "테스트 유저", pageable);

            // Then
            assertThat(result.content()).hasSize(1);
            verify(noticeRepository).findAllByKeyword(any(Specification.class), eq(pageable));
        }

        @Test
        @DisplayName("키워드 검색 - 전체")
        void getNoticesByKeyword_all() {
            // Given
            Pageable pageable = PageRequest.of(0, 10);
            Notice notice = createNotice(1L, 1L);
            Page<Notice> noticePage = new PageImpl<>(List.of(notice));

            when(noticeRepository.findAllByKeyword(any(Specification.class), eq(pageable)))
                    .thenReturn(noticePage);

            // When
            PageResponse<NoticeSummaryResponse> result =
                    readNoticeService.getNoticesByKeyword("all", "테스트 유저", pageable);

            // Then
            assertThat(result.content()).hasSize(1);
            verify(noticeRepository).findAllByKeyword(any(Specification.class), eq(pageable));
        }
    }
}
