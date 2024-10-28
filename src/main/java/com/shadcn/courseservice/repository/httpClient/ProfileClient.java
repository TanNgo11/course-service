package com.shadcn.courseservice.repository.httpClient;

import java.util.List;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;

import com.shadcn.courseservice.config.AuthenticationRequestInterceptor;
import com.shadcn.courseservice.dto.response.ApiResponse;
import com.shadcn.courseservice.dto.response.StudentProfileResponse;
import com.shadcn.courseservice.dto.response.TeacherProfileResponse;
import com.shadcn.courseservice.exception.RetreiveMessageErrorDecoder;

@FeignClient(
        name = "profile",
        url = "${app.services.profile}",
        configuration = {AuthenticationRequestInterceptor.class, RetreiveMessageErrorDecoder.class})
public interface ProfileClient {
    @GetMapping(value = "/api/v1/users/students/ids", produces = MediaType.APPLICATION_JSON_VALUE)
    ApiResponse<List<StudentProfileResponse>> getPublicStudentProfile(@RequestParam long[] studentIds);

    @GetMapping(value = "/api/v1/users/teachers/ids", produces = MediaType.APPLICATION_JSON_VALUE)
    ApiResponse<List<TeacherProfileResponse>> getPublicTeacherProfile(@RequestParam long[] teacherIds);
}
