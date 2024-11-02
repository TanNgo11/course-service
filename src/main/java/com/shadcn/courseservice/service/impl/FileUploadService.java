package com.shadcn.courseservice.service.impl;

import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;

import com.shadcn.courseservice.dto.response.FileUploadResponse;
import com.shadcn.courseservice.repository.httpClient.FileServiceClient;
import com.shadcn.courseservice.service.IFileUploadService;

import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;

@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@Component
public class FileUploadService implements IFileUploadService {
    FileServiceClient fileServiceClient;

    @Override
    public String uploadFileIfPresent(MultipartFile imageFile) {
        if (imageFile != null) {
            FileUploadResponse imageResponse =
                    fileServiceClient.uploadFile(imageFile).getResult();

            return imageResponse.getDownloadUri();
        }
        return null;
    }
}
