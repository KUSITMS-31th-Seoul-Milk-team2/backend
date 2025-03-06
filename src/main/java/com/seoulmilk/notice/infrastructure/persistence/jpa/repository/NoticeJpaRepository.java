package com.seoulmilk.notice.infrastructure.persistence.jpa.repository;

import com.seoulmilk.notice.dto.request.UpdateNoticeRequest;
import com.seoulmilk.notice.infrastructure.persistence.jpa.entity.NoticeJpaEntity;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.transaction.annotation.Transactional;


public interface NoticeJpaRepository extends JpaRepository<NoticeJpaEntity, Long> {

    @Transactional
    @Query("SELECT n FROM NoticeJpaEntity n ORDER BY n.id DESC")
    Page<NoticeJpaEntity> findAllOrderByIdDesc(Pageable pageable);

    @Modifying(clearAutomatically = true)
    @Transactional
    @Query("UPDATE NoticeJpaEntity n SET n.title = :#{#updateNoticeRequest.title}, n.content = :#{#updateNoticeRequest.content} WHERE n.id = :#{#updateNoticeRequest.id}")
    int update(UpdateNoticeRequest updateNoticeRequest);
}
