package com.seoulmilk.notice.domain.repository;

import com.seoulmilk.notice.domain.entity.Notice;
import com.seoulmilk.notice.dto.request.UpdateNoticeRequest;
import org.springframework.data.domain.Page;

import org.springframework.data.domain.Pageable;

import java.util.Optional;

public interface NoticeRepository {
    Notice save(Notice notice);

    Optional<Notice> findById(Long id);

    void delete(Notice notice);

    Page<Notice> findAllWithAuthor(Pageable pageable);

    Notice updateNotice(UpdateNoticeRequest updateNoticeRequest, String fileUrl);

}
