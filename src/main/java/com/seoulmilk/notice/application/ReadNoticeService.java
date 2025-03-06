package com.seoulmilk.notice.application;

import com.seoulmilk.emp.domain.entity.Emp;
import com.seoulmilk.emp.domain.repository.EmpRepository;
import com.seoulmilk.emp.exception.EmpErrorCode;
import com.seoulmilk.notice.domain.entity.Notice;
import com.seoulmilk.notice.domain.repository.NoticeRepository;
import com.seoulmilk.notice.dto.response.NoticeSummaryResponse;
import com.seoulmilk.notice.dto.response.PageNoticeResponse;
import com.seoulmilk.notice.dto.response.ReadNoticeResponse;
import com.seoulmilk.notice.exception.NoticeErrorCode;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Service;

import org.springframework.data.domain.Pageable;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class ReadNoticeService {
    private final NoticeRepository noticeRepository;
    private final EmpRepository empRepository;

    public ReadNoticeResponse readOneNotice(Long id) {
        Notice notice = noticeRepository.findById(id)
                .orElseThrow(NoticeErrorCode.NOT_EXISTS_NOTICE::toException);

        Emp author = empRepository.findByEmployeeId(notice.getEmployeeId())
                .orElseThrow(EmpErrorCode.NOT_EXIST_EMPLOYEE::toException);

        return ReadNoticeResponse.create(notice, author);
    }

    public PageNoticeResponse<NoticeSummaryResponse> getNoticesByPage(Pageable pageable) {
        Page<Notice> notices = noticeRepository.findAllOrderByIdDesc(pageable);

        List<NoticeSummaryResponse> content = notices.getContent().stream()
                .map(notice -> {
                    Emp author = empRepository.findByEmployeeId(notice.getEmployeeId())
                            .orElseThrow(EmpErrorCode.NOT_EXIST_EMPLOYEE::toException);

                    return NoticeSummaryResponse.create(notice, author);
                }).toList();

        return PageNoticeResponse.create(content, notices);
    }


}
