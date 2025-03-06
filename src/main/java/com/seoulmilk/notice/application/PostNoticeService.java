package com.seoulmilk.notice.application;

import com.seoulmilk.core.infrastructure.security.CustomUserDetails;
import com.seoulmilk.core.util.fileUtil.FileUtil;
import com.seoulmilk.notice.domain.entity.Notice;
import com.seoulmilk.notice.domain.repository.NoticeRepository;
import com.seoulmilk.notice.dto.request.PostNoticeRequest;
import com.seoulmilk.notice.dto.response.PostNoticeResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

@Service
@Log4j2
@RequiredArgsConstructor
public class PostNoticeService {

    private final NoticeRepository noticeRepository;
    private final FileUtil fileUtil;

    public PostNoticeResponse post(CustomUserDetails customUserDetails, PostNoticeRequest postNoticeRequest, MultipartFile file) {
        String employeeId = customUserDetails.emp().getEmployeeId();
        String fileUrl = fileUtil.uploadFile(file);
        Notice notice = noticeRepository.save(
                Notice.create(employeeId, postNoticeRequest.title(), postNoticeRequest.content(), fileUrl)
        );

        return PostNoticeResponse.create(notice);
    }
}
