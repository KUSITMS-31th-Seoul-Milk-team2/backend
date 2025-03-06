package com.seoulmilk.core.application;

import org.springframework.web.multipart.MultipartFile;

public interface FileStorageService {
    String uploadFile(MultipartFile file) throws Exception;
}
