package com.shadcn.courseservice.repository.httpclient;

import com.shadcn.courseservice.config.AuthenticationRequestInterceptor;
import com.shadcn.courseservice.dto.response.ApiResponse;
import com.shadcn.courseservice.dto.response.ImageUploadResponse;
import com.shadcn.courseservice.exception.RetreiveMessageErrorDecoder;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.multipart.MultipartFile;

@FeignClient(
        name = "file-service",
        url = "${app.services.file}",
        configuration = {AuthenticationRequestInterceptor.class})
public interface FileServiceClient {
    @PostMapping(value = "/upload", headers = "Content-Type: multipart/form-data", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    ApiResponse<ImageUploadResponse> uploadFile(@RequestPart(value="file") MultipartFile file);
}
