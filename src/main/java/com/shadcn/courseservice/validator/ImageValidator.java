package com.shadcn.courseservice.validator;

import com.shadcn.courseservice.dto.response.ApiResponse;
import com.shadcn.courseservice.dto.response.ImageUploadResponse;
import com.shadcn.courseservice.repository.httpclient.FileServiceClient;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;

@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@Component
public class ImageValidator {
    FileServiceClient fileServiceClient;

    public String uploadImageIfPresent(MultipartFile imageFile) {
        if (imageFile != null && !imageFile.isEmpty()) {
            ApiResponse<ImageUploadResponse> response = fileServiceClient.uploadFile(imageFile);
            ImageUploadResponse imageResponse = response.getResult();

            if (imageResponse != null) {
                return imageResponse.getDownloadUri(); // Return the image URL to be saved in the Course entity
            }
        }
        return null;
    }
}