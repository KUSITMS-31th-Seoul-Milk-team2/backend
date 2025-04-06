package com.seoulmilk.notice.domain.repository;

import com.seoulmilk.emp.domain.entity.Emp;
import com.seoulmilk.notice.domain.entity.Notice;
import com.seoulmilk.notice.dto.request.Order;
import com.seoulmilk.notice.dto.request.UpdateNoticeRequest;
import com.seoulmilk.notice.infrastructure.persistence.jpa.entity.NoticeJpaEntity;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;

import java.util.List;
import java.util.Optional;

public interface NoticeRepository {
    Notice save(Notice notice);

    Optional<Notice> findById(Long id);

    void delete(Notice notice);

    Page<Notice> findAllOrderByIdDescAndEmpPk(Long empPk, Pageable pageable);

    Page<Notice> findAllOrderByIdDesc(Pageable pageable);

    Notice updateNotice(UpdateNoticeRequest updateNoticeRequest, String fileUrl);

    Page<Notice> findAllByKeyword(Specification<NoticeJpaEntity> spec, Pageable pageable);

    Page<Notice> findAllByAuthorName(String keyword, Pageable pageable);

    List<Notice> findAllByIds(List<Long> ids);

    void deleteAll(List<Notice> notices);

    void deleteAllNoticesByEmps(List<Emp> emps);

    List<Notice> findAllByCursorPagination(Long key, Order order__createdAt, Long take);
}
