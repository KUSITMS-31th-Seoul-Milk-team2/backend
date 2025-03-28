package com.seoulmilk.notice.infrastructure.persistence.jpa.repository;

import com.seoulmilk.notice.dto.request.UpdateNoticeRequest;
import com.seoulmilk.notice.infrastructure.persistence.jpa.entity.NoticeJpaEntity;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;


public interface NoticeJpaRepository extends JpaRepository<NoticeJpaEntity, Long>, JpaSpecificationExecutor<NoticeJpaEntity> {

    @Query("SELECT n FROM NoticeJpaEntity n WHERE n.authorPk = :empPk")
    Page<NoticeJpaEntity> findAllOrderByIdDescAndId(Pageable pageable, Long empPk);

    @Query("SELECT n FROM NoticeJpaEntity n")
    Page<NoticeJpaEntity> findAllOrderByIdDesc(Pageable pageable);

    @Modifying(clearAutomatically = true)
    @Transactional
    @Query("UPDATE NoticeJpaEntity n SET n.title = :#{#updateNoticeRequest.title}, n.content = :#{#updateNoticeRequest.content} WHERE n.id = :#{#updateNoticeRequest.id}")
    int update(UpdateNoticeRequest updateNoticeRequest);

    List<NoticeJpaEntity> findAllByIdIn(List<Long> ids);

    @Modifying(clearAutomatically = true)
    @Query("DELETE FROM NoticeJpaEntity n WHERE n.authorPk IN :empPks")
    void deleteAllByAuthorPk(@Param("empPks") List<Long> empPks);
}

