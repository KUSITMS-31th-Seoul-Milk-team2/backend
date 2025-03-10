package com.seoulmilk.notice.application;

import com.seoulmilk.core.infrastructure.security.CustomUserDetails;
import com.seoulmilk.notice.domain.entity.Notice;
import com.seoulmilk.notice.domain.repository.NoticeRepository;
import com.seoulmilk.notice.dto.request.DeleteNoticeRequest;
import com.seoulmilk.notice.exception.NoticeErrorCode;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
@Log4j2
public class DeleteNoticeService {

    private final NoticeRepository noticeRepository;

    @Transactional
    public void delete(CustomUserDetails customUserDetails, DeleteNoticeRequest deleteNoticeRequest) {
        List<Notice> notices = noticeRepository.findAllByIds(deleteNoticeRequest.ids());
        for (Notice notice : notices) {
            if (!notice.isAuthor(customUserDetails.getId())) {
                throw NoticeErrorCode.NOT_AN_AUTHOR.toException();
            }
        }
        noticeRepository.deleteAll(notices);
    }
}
