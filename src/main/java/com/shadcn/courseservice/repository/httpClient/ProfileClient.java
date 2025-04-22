package com.shadcn.courseservice.repository.httpClient;

import java.util.List;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;

import com.shadcn.courseservice.config.AuthenticationRequestInterceptor;
import com.shadcn.courseservice.dto.response.ApiResponse;
import com.shadcn.courseservice.dto.response.student.StudentProfileResponse;
import com.shadcn.courseservice.dto.response.teacher.TeacherProfileResponse;
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

    @GetMapping(
            value = "/api/v1/users/students/student-entity-id/{studentId}",
            produces = MediaType.APPLICATION_JSON_VALUE)
    ApiResponse<StudentProfileResponse> getStudentProfileByStudentEntityId(@PathVariable String studentId);

    @GetMapping(
            value = "/api/v1/users/teachers/teacher-entity-id/{teacherId}",
            produces = MediaType.APPLICATION_JSON_VALUE)
    ApiResponse<TeacherProfileResponse> getStudentProfileByTeacherEntityId(@PathVariable String teacherId);

    @GetMapping(value = "/api/v1/users/teachers/entity-ids", produces = MediaType.APPLICATION_JSON_VALUE)
    ApiResponse<List<TeacherProfileResponse>> getTeacherProfilesByEntityIds(@RequestParam long[] teacherIds);

    @GetMapping(value = "/api/v1/users/students/entity-ids", produces = MediaType.APPLICATION_JSON_VALUE)
    ApiResponse<List<StudentProfileResponse>> getStudentProfilesByEntityIds(@RequestParam long[] studentIds);
}
