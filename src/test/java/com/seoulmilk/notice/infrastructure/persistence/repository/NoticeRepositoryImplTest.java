package com.seoulmilk.notice.infrastructure.persistence.repository;

import com.seoulmilk.core.exception.DomainException;
import com.seoulmilk.notice.domain.entity.Notice;
import com.seoulmilk.notice.infrastructure.mapper.NoticeMapper;
import com.seoulmilk.notice.infrastructure.persistence.jpa.entity.NoticeJpaEntity;
import com.seoulmilk.notice.infrastructure.persistence.jpa.repository.NoticeJpaRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class NoticeRepositoryImplTest {
    @Mock
    private NoticeMapper noticeMapper;

    @Mock
    private NoticeJpaRepository noticeJpaRepository;

    @InjectMocks
    NoticeRepositoryImpl noticeRepositoryImpl;

    @DisplayName("save() 테스트")
    @Nested
    class SaveTest {
        @Nested
        class Success {

            @DisplayName("정상적으로 저장된다.")
            @Test
            void save_success() {
                // given
                Notice notice = Notice.builder()
                        .title("title")
                        .content("content")
                        .build();

                NoticeJpaEntity noticeJpaEntity = NoticeJpaEntity.builder()
                        .title("title")
                        .content("content")
                        .build();
                // when
                when(noticeMapper.toJpaEntity(notice)).thenReturn(noticeJpaEntity);
                when(noticeJpaRepository.save(noticeJpaEntity)).thenReturn(noticeJpaEntity);
                when(noticeMapper.toDomainEntity(noticeJpaEntity)).thenReturn(notice);

                Notice result = noticeRepositoryImpl.save(notice);

                // then
                assertThat(result).isNotNull();
                assertThat(result).isEqualTo(notice);
            }
        }

        @Nested
        class Failure {
            @DisplayName("JpaEntity가 null인 경우 예외가 발생한다.")
            @Test
            void save_fail() {
                // given
                Notice notice = Notice.builder()
                        .title("title")
                        .content("content")
                        .build();

                // when
                when(noticeMapper.toJpaEntity(notice)).thenReturn(null);

                // then
                try {
                    noticeRepositoryImpl.save(notice);
                } catch (Exception e) {
                    assertThat(e).isInstanceOf(DomainException.class);
                    assertThat(e.getMessage()).isEqualTo("존재하지 않는 공지사항입니다.");
                }
            }
        }
    }

    @DisplayName("findById() 테스트")
    @Test
    void findById_success() {
        // given
        Long id = 1L;
        NoticeJpaEntity noticeJpaEntity = NoticeJpaEntity.builder()
                .id(id)
                .title("title")
                .content("content")
                .build();
        Optional<Notice> optionalNotice = Optional.of(Notice.builder()
                .id(id)
                .title("title")
                .content("content")
                .build());
        // when
        when(noticeJpaRepository.findById(id)).thenReturn(Optional.of(noticeJpaEntity));
        when(noticeMapper.toDomainEntity(noticeJpaEntity)).thenReturn(optionalNotice.get());

        Optional<Notice> result = noticeRepositoryImpl.findById(id);

        // then

        assertThat(result).isPresent();
        assertThat(result.get().getId()).isEqualTo(id);
    }

    @DisplayName("delete() 테스트")
    @Nested
    class DeleteTest {
        @DisplayName("정상적으로 삭제된다.")
        @Test
        void delete_success() {
            // given
            Notice notice = Notice.builder()
                    .id(1L)
                    .title("title")
                    .content("content")
                    .build();

            NoticeJpaEntity noticeJpaEntity = NoticeJpaEntity.builder()
                    .id(1L)
                    .title("title")
                    .content("content")
                    .build();
            // when
            when(noticeMapper.toJpaEntity(notice)).thenReturn(noticeJpaEntity);
            noticeRepositoryImpl.delete(notice);

            // then
             verify(noticeJpaRepository).delete(noticeJpaEntity);
        }

        @DisplayName("삭제 중 예외가 발생한다.")
        @Test
        void delete_failed() {
            // given
            Notice notice = Notice.builder()
                    .id(1L)
                    .title("title")
                    .content("content")
                    .build();

            NoticeJpaEntity noticeJpaEntity = NoticeJpaEntity.builder()
                    .id(1L)
                    .title("title")
                    .content("content")
                    .build();
            // when
            when(noticeMapper.toJpaEntity(notice)).thenThrow(new RuntimeException("삭제 중 예외 발생"));

            // then
            try {
                noticeRepositoryImpl.delete(notice);
            } catch (Exception e) {
                assertThat(e.getMessage()).isEqualTo("알 수 없는 내부 오류입니다.");
            }
        }
    }

}
