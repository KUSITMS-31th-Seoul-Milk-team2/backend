package com.seoulmilk.notice.infrastructure.mapper;

import com.seoulmilk.notice.domain.entity.Notice;
import com.seoulmilk.notice.infrastructure.persistence.jpa.entity.NoticeJpaEntity;

public class NoticeMapper {
    public Notice toDomainEntity(NoticeJpaEntity notice) {
        return Notice.toDomainEntity(notice);
    }

    public NoticeJpaEntity toJpaEntity(Notice notice) {
        return NoticeJpaEntity.toJpaEntity(notice);
    }
}
