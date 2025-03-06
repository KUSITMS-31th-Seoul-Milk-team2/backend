package com.seoulmilk.core.util.fileUtil;

import com.seoulmilk.core.application.FileStorageService;
import com.seoulmilk.core.exception.error.GlobalErrorCode;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.apache.commons.fileupload.FileUploadException;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;

import java.util.Optional;

@Component
@RequiredArgsConstructor
@Log4j2
public class FileUtil {
    private final FileStorageService fileStorageService;

    public String uploadFile(MultipartFile file) {
        return uploadToStorage(file).orElse(null);
    }

    private Optional<String> uploadToStorage(MultipartFile file) {
        return Optional.ofNullable(file)
                .map((this::executeFileUpload));
    }

    private String executeFileUpload(MultipartFile file) {
        try {
            return fileStorageService.uploadFile(file);
        } catch (FileUploadException e) {
            log.error("[FileUtil] 써드파티 저장소에 파일 업로드 중 오류가 발생했습니다.", e);
            throw GlobalErrorCode.FILE_UPLOAD_ERROR.toException();
        } catch (Exception e) {
            log.error("[FileUtil] 써드파티 저장소에 파일 업로드 중 시스템 내부 에러가 발생했습니다..", e);
            throw GlobalErrorCode.INTERNAL_SERVER_ERROR.toException();
        }
    }

}
