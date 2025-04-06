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

    @Query(value = """
            SELECT
                n.id,
                n.author_pk,
                n.author_name,
                n.title,
                n.content,
                n.file_url,
                n.created_at,
                n.updated_at,
                n.deleted
            FROM Notice n
            WHERE CONTAINS(n.author_name, :keyword) > 0
            ORDER BY n.id DESC
            OFFSET :#{#pageable.offset} ROWS FETCH NEXT :#{#pageable.pageSize} ROWS ONLY
            """,
            countQuery = """
                    SELECT COUNT(n.id)
                    FROM Notice n
                    WHERE CONTAINS(n.author_name, :keyword) > 0
                    """,
            nativeQuery = true)
    Page<NoticeJpaEntity> findAll(@Param("keyword") String keyword, @Param("pageable") Pageable pageable);

    @Query("SELECT n FROM NoticeJpaEntity n WHERE n.authorPk = :empPk")
    Page<NoticeJpaEntity> findAllOrderByIdDescAndId(Pageable pageable, Long empPk);

    Page<NoticeJpaEntity> findAllByOrderByIdDesc(Pageable pageable);

    @Modifying(clearAutomatically = true)
    @Transactional
    @Query("UPDATE NoticeJpaEntity n SET n.title = :#{#updateNoticeRequest.title}, n.content = :#{#updateNoticeRequest.content} WHERE n.id = :#{#updateNoticeRequest.id}")
    int update(UpdateNoticeRequest updateNoticeRequest);

    List<NoticeJpaEntity> findAllByIdIn(List<Long> ids);

    @Modifying(clearAutomatically = true)
    @Query("DELETE FROM NoticeJpaEntity n WHERE n.authorPk IN :empPks")
    void deleteAllByAuthorPk(@Param("empPks") List<Long> empPks);

    @Query(value = """
            SELECT /*+ INDEX_RS_DESC(n idx_notice_id) */
                n.id,
                n.author_pk,
                n.author_name,
                n.title,
                n.content,
                n.file_url,
                n.created_at,
                n.updated_at,
                n.deleted
            FROM notice n
            WHERE n.id < ?1
            ORDER BY n.id DESC
            FETCH FIRST ?2 ROWS ONLY
            """, nativeQuery = true)
    List<NoticeJpaEntity> findAllByPaginationDesc(Long key, Long take);


    @Query(value = """
            SELECT * FROM (
                SELECT * FROM notice
                WHERE id > ?1
                ORDER BY created_at ASC, id ASC
            ) 
            WHERE ROWNUM <= ?2
            """,
            nativeQuery = true)
    List<NoticeJpaEntity> findAllByPaginationAsc(Long key, Long take);
}

