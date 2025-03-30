package com.seoulmilk.notice.application;

import com.seoulmilk.core.application.CacheService;
import com.seoulmilk.emp.domain.entity.Emp;
import com.seoulmilk.emp.domain.repository.EmpRepository;
import com.seoulmilk.emp.exception.EmpErrorCode;
import com.seoulmilk.notice.domain.entity.Notice;
import com.seoulmilk.notice.domain.repository.NoticeRepository;
import com.seoulmilk.notice.domain.value.Keywords;
import com.seoulmilk.notice.dto.request.ReadNoticePaginationRequest;
import com.seoulmilk.notice.dto.response.NoticeSummaryResponse;
import com.seoulmilk.notice.dto.response.PageNoticeResponse;
import com.seoulmilk.notice.dto.response.ReadNoticeResponse;
import com.seoulmilk.notice.dto.response.ReadPaginatedResponse;
import com.seoulmilk.notice.exception.NoticeErrorCode;
import com.seoulmilk.notice.infrastructure.persistence.jpa.entity.NoticeJpaEntity;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class ReadNoticeService {
    private final NoticeRepository noticeRepository;
    private final EmpRepository empRepository;
    private final CacheService cacheService;

    @Value("${spring.url.base}")
    private String baseUrl;

    public PageNoticeResponse<NoticeSummaryResponse> getMyNotices(Long empPk, Pageable pageable) {
        Page<Notice> notices = noticeRepository.findAllOrderByIdDescAndEmpPk(empPk, pageable);
        List<NoticeSummaryResponse> content = notices.getContent().stream()
                .map(NoticeSummaryResponse::create).toList();
        return PageNoticeResponse.create(content, notices);
    }

    public ReadNoticeResponse readOneNotice(Long id) {
        Notice notice = noticeRepository.findById(id)
                .orElseThrow(NoticeErrorCode.NOT_EXISTS_NOTICE::toException);

        Emp author = empRepository.findById(notice.getAuthorPk())
                .orElseThrow(EmpErrorCode.NOT_EXIST_EMPLOYEE::toException);

        return ReadNoticeResponse.create(notice, author);
    }

    @Cacheable(
            value = "notice",
            cacheManager = "customCacheManager",
            keyGenerator = "noticePageableKeyGenerator"
    )
    public PageNoticeResponse<NoticeSummaryResponse> getNoticesByPage(Pageable pageable) {
        cacheService.checkCacheContent();
        log.info("[ReadNoticeService.getNoticesByPage] 공지사항 페이지를 조회합니다.");
        Page<Notice> notices = noticeRepository.findAllOrderByIdDesc(pageable);
        List<NoticeSummaryResponse> content = notices.getContent().stream()
                .map(NoticeSummaryResponse::create).toList();

        return PageNoticeResponse.create(content, notices);
    }

    public PageNoticeResponse<NoticeSummaryResponse> getNoticesByKeyword(String searchType, String keyword, Pageable pageable) {
        Specification<NoticeJpaEntity> spec = Keywords.fromValue(searchType).getSpecification(keyword);
        Page<Notice> notices = noticeRepository.findAllByKeyword(spec, pageable);
        List<NoticeSummaryResponse> content = notices.getContent().stream()
                .map(NoticeSummaryResponse::create).toList();

        return PageNoticeResponse.create(content, notices);
    }

    public ReadPaginatedResponse<NoticeSummaryResponse> cursorPaginateNotices(ReadNoticePaginationRequest readNoticePaginationRequest) {
        List<NoticeSummaryResponse> notices = noticeRepository.findAllByCursorPagination(
                        readNoticePaginationRequest.getKey(),
                        readNoticePaginationRequest.getOrder__createdAt(),
                        readNoticePaginationRequest.getTake()
                ).stream()
                .map(NoticeSummaryResponse::create)
                .toList();

        return ReadPaginatedResponse.create(
                notices,
                notices.getLast().id(),
                notices.size(),
                baseUrl + "/v1/notice/list?order__createdAt=" + readNoticePaginationRequest.getOrder__createdAt() + "&take=" + readNoticePaginationRequest.getTake() + "&key=" + notices.getLast().id()
        );
    }
}
