package com.seoulmilk.notice.domain.entity;

import com.seoulmilk.notice.infrastructure.persistence.jpa.entity.NoticeJpaEntity;
import lombok.Builder;
import lombok.Getter;

import java.time.LocalDateTime;
import java.util.Objects;

@Getter
@Builder(toBuilder = true)
public class Notice {
    private Long id;

    private Long authorPk;

    private String authorName;

    private String title;

    private String content;

    private String fileUrl;

    private LocalDateTime createdAt;

    private LocalDateTime updatedAt;

    private Boolean deleted;

    public static Notice create(Long authorPk, String authorName, String title, String content, String fileUrl) {
        return Notice.builder()
                .id(null)
                .authorPk(authorPk)
                .authorName(authorName)
                .title(title)
                .content(content)
                .fileUrl(fileUrl)
                .build();
    }

    public Notice update(String title, String content, String fileUrl) {
        return this.toBuilder()
                .title(title != null ? title : this.title)
                .content(content != null ? content : this.content)
                .fileUrl(fileUrl != null ? fileUrl : this.fileUrl)
                .updatedAt(LocalDateTime.now())  // 업데이트 시간 자동 반영
                .build();
    }

    public static Notice toDomainEntity(NoticeJpaEntity noticeJpaEntity) {
        return Notice.builder()
                .id(noticeJpaEntity.getId())
                .authorPk(noticeJpaEntity.getAuthorPk())
                .authorName(noticeJpaEntity.getAuthorName())
                .title(noticeJpaEntity.getTitle())
                .content(noticeJpaEntity.getContent())
                .fileUrl(noticeJpaEntity.getFileUrl())
                .createdAt(noticeJpaEntity.getCreatedAt())
                .updatedAt(noticeJpaEntity.getUpdatedAt())
                .deleted(noticeJpaEntity.getDeleted())
                .build();
    }

    public boolean isAuthor(Long empPk) {
        return Objects.equals(this.authorPk, empPk);
    }
}
