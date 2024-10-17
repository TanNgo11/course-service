package com.shadcn.courseservice.service.impl;

import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;

import com.shadcn.courseservice.dto.response.ImageUploadResponse;
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
    public String uploadImageIfPresent(MultipartFile imageFile) {
        if (imageFile != null) {
            //            ApiResponse<ImageUploadResponse> response = fileServiceClient.uploadFile(imageFile);
            //            ImageUploadResponse imageResponse = response.getResult();
            //
            //            if (imageResponse != null) {
            //                return imageResponse.getDownloadUri(); // Return the image URL to be saved in the Course
            // entity
            //            }
            ImageUploadResponse imageResponse =
                    fileServiceClient.uploadFile(imageFile).getResult();

            return imageResponse.getDownloadUri();
        }
        return null;
    }
}
