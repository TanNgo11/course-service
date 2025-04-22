package com.shadcn.courseservice.repository.httpClient;

import java.util.List;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.multipart.MultipartFile;

import com.shadcn.courseservice.config.AuthenticationRequestInterceptor;
import com.shadcn.courseservice.dto.response.ApiResponse;
import com.shadcn.courseservice.dto.response.file.FileUploadResponse;
import com.shadcn.courseservice.exception.RetreiveMessageErrorDecoder;

@FeignClient(
        name = "file-service",
        url = "${app.services.file}",
        configuration = {AuthenticationRequestInterceptor.class, RetreiveMessageErrorDecoder.class})
public interface FileServiceClient {
    @PostMapping(
            value = "/upload",
            headers = "Content-Type: multipart/form-data",
            consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    ApiResponse<FileUploadResponse> uploadFile(@RequestPart(value = "file") MultipartFile file);

    @PostMapping(
            value = "/upload/multiple",
            headers = "Content-Type: multipart/form-data",
            consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    ApiResponse<List<FileUploadResponse>> uploadMultipleFiles(@RequestPart(value = "files") MultipartFile[] file);
}
