package com.seoulmilk.notice.domain.entity;

import com.seoulmilk.notice.infrastructure.persistence.jpa.entity.NoticeJpaEntity;
import lombok.Builder;
import lombok.Getter;

import java.time.LocalDateTime;

@Getter
@Builder
public class Notice {
    private Long id;

    private String employeeId;

    private String title;

    private String content;

    private String fileUrl;

    private LocalDateTime createdAt;

    private LocalDateTime updatedAt;

    private Boolean deleted;

    public static Notice create(String employeeId, String title, String content, String fileUrl) {
        return Notice.builder()
                .id(null)
                .employeeId(employeeId)
                .title(title)
                .content(content)
                .fileUrl(fileUrl)
                .build();
    }

    public static Notice toDomainEntity(NoticeJpaEntity noticeJpaEntity) {
        return Notice.builder()
                .id(noticeJpaEntity.getId())
                .employeeId(noticeJpaEntity.getEmployeeId())
                .title(noticeJpaEntity.getTitle())
                .content(noticeJpaEntity.getContent())
                .fileUrl(noticeJpaEntity.getFileUrl())
                .createdAt(noticeJpaEntity.getCreatedAt())
                .updatedAt(noticeJpaEntity.getUpdatedAt())
                .deleted(noticeJpaEntity.getDeleted())
                .build();
    }
}
