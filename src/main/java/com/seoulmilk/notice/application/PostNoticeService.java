package com.seoulmilk.notice.application;

import com.seoulmilk.core.infrastructure.security.CustomUserDetails;
import com.seoulmilk.core.util.fileUtil.FileUtil;
import com.seoulmilk.notice.domain.entity.Notice;
import com.seoulmilk.notice.domain.repository.NoticeRepository;
import com.seoulmilk.notice.dto.request.PostNoticeRequest;
import com.seoulmilk.notice.dto.response.PostNoticeResponse;
import com.seoulmilk.notice.exception.NoticeErrorCode;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

@Service
@Log4j2
@RequiredArgsConstructor
public class PostNoticeService {

    private final NoticeRepository noticeRepository;
    private final FileUtil fileUtil;

    @Transactional
    public PostNoticeResponse post(CustomUserDetails customUserDetails, PostNoticeRequest postNoticeRequest, MultipartFile file) {
        String fileUrl = fileUtil.uploadFile(file);
        try {
            Notice notice = noticeRepository.save(
                    Notice.create(customUserDetails.getId(), customUserDetails.getUsername(), postNoticeRequest.title(), postNoticeRequest.content(), fileUrl)
            );

            return PostNoticeResponse.create(notice);
        } catch (Exception e) {
            log.error("[PostNoticeService.post] 공지사항을 등록하는데 실패했습니다. {}", e.getMessage());
            throw NoticeErrorCode.POST_NOTICE_FAILED.toException();
        }
    }
}
