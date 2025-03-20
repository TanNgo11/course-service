package com.shadcn.courseservice.repository.httpClient;

import java.util.List;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;

import com.shadcn.courseservice.config.AuthenticationRequestInterceptor;
import com.shadcn.courseservice.dto.response.ApiResponse;
import com.shadcn.courseservice.dto.response.StudentProfileResponse;
import com.shadcn.courseservice.dto.response.UserProfileResponse;
import com.shadcn.courseservice.exception.RetreiveMessageErrorDecoder;

@FeignClient(
        name = "identity-service",
        url = "${app.services.identity}",
        configuration = {AuthenticationRequestInterceptor.class, RetreiveMessageErrorDecoder.class})
public interface IdentityClient {
    @GetMapping(value = "/api/v1/users/me", produces = MediaType.APPLICATION_JSON_VALUE)
    ApiResponse<UserProfileResponse> getCurrentUserProfile();

    @GetMapping(value = "/api/v1/users/admins/profile/{userId}", produces = MediaType.APPLICATION_JSON_VALUE)
    ApiResponse<StudentProfileResponse> getStudentProfileById(@PathVariable Long userId);

    @GetMapping(value = "/api/v1/users/teachers/profile/{userId}", produces = MediaType.APPLICATION_JSON_VALUE)
    ApiResponse<StudentProfileResponse> getTeacherProfileById(@PathVariable Long userId);

    @GetMapping(value = "/api/v1/users/profiles", produces = MediaType.APPLICATION_JSON_VALUE)
    ApiResponse<List<UserProfileResponse>> getUserProfileResponses(@RequestParam List<Long> userIds);
}
