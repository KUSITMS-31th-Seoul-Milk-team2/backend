package com.seoulmilk.notice.application;

import com.seoulmilk.core.infrastructure.security.CustomUserDetails;
import com.seoulmilk.core.util.fileUtil.FileUtil;
import com.seoulmilk.notice.domain.entity.Notice;
import com.seoulmilk.notice.domain.repository.NoticeRepository;
import com.seoulmilk.notice.dto.request.UpdateNoticeRequest;
import com.seoulmilk.notice.exception.NoticeErrorCode;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

@Service
@Log4j2
@RequiredArgsConstructor
public class UpdateNoticeService {

    private final NoticeRepository noticeRepository;
    private final FileUtil fileUtil;

    @Transactional
    public void updateNotice(CustomUserDetails customUserDetails, UpdateNoticeRequest updateNoticeRequest, MultipartFile file) {
        Notice notice = noticeRepository.findById(updateNoticeRequest.id())
                .orElseThrow(NoticeErrorCode.NOT_EXISTS_NOTICE::toException);

        if (!notice.isAuthor(customUserDetails.getId())) {
            throw NoticeErrorCode.NOT_AN_AUTHOR.toException();
        }

        String fileUrl = fileUtil.uploadFile(file);

        noticeRepository.updateNotice(updateNoticeRequest, fileUrl);
    }
}
