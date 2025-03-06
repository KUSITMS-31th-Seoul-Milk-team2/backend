package com.seoulmilk.notice.infrastructure.persistence.jpa.entity;

import com.seoulmilk.core.infrastructure.jpa.entity.BaseLongIdEntity;
import com.seoulmilk.notice.domain.entity.Notice;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

import java.util.Optional;

@Entity
@Getter
@SuperBuilder
@Table(name = "NOTICE")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class NoticeJpaEntity extends BaseLongIdEntity {
    private String employeeId;

    private String title;

    private String content;

    @Column(length = 512)
    private String fileUrl;

    public static NoticeJpaEntity toJpaEntity(Notice notice) {
        return NoticeJpaEntity.builder()
                .id(notice.getId())
                .employeeId(notice.getEmployeeId())
                .title(notice.getTitle())
                .content(notice.getContent())
                .fileUrl(notice.getFileUrl())
                .createdAt(notice.getCreatedAt())
                .updatedAt(notice.getUpdatedAt())
                .deleted(Optional.ofNullable(notice.getDeleted()).orElse(false))
                .build();
    }

}
