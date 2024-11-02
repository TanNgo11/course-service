package com.shadcn.courseservice.service;

import org.springframework.web.multipart.MultipartFile;

public interface IFileUploadService {
    String uploadFileIfPresent(MultipartFile imageFile);
}
