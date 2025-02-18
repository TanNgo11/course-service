package com.shadcn.courseservice.controller;

import static com.shadcn.courseservice.constant.PathConstant.API_V1_REGISTRATIONS;

import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import com.shadcn.courseservice.dto.request.RegistrationRequest;
import com.shadcn.courseservice.dto.response.ApiResponse;
import com.shadcn.courseservice.dto.response.PageResponse;
import com.shadcn.courseservice.dto.response.RegistrationResponse;
import com.shadcn.courseservice.service.IRegistrationService;

import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;

@RestController
@RequestMapping(API_V1_REGISTRATIONS)
@RequiredArgsConstructor
@Slf4j
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class RegistrationController {
    IRegistrationService registrationService;

    @PostMapping("/student-registrations")
    public ApiResponse<RegistrationResponse> registerStudentToCourse(@RequestParam RegistrationRequest request) {
        return ApiResponse.success(registrationService.registerStudentToCourse(
                request.getStudentId(), request.getCourseId(), request.getSemesterId()));
    }

    @DeleteMapping("/unregister-student")
    @PreAuthorize("hasRole('ADMIN') or hasRole('STUDENT')")
    public ApiResponse<Void> unregisterStudentFromCourse(@RequestParam long studentId, @RequestParam long courseId) {
        registrationService.unregisterStudentsFromCourseForStudent(studentId, courseId);
        return ApiResponse.empty();
    }

    @DeleteMapping("/admin-unregister-student")
    @PreAuthorize("hasRole('ADMIN')")
    public ApiResponse<Void> adminUnregisterStudentFromCourse(
            @RequestParam long studentId, @RequestParam long courseId) {
        registrationService.unregisterStudentsFromCourseForAdmin(studentId, courseId);
        return ApiResponse.empty();
    }

    @DeleteMapping("/unregister-all")
    @PreAuthorize("hasRole('ADMIN')")
    public ApiResponse<Void> unregisterAllCoursesFromStudent(@RequestParam long studentId) {
        registrationService.unregisterAllCoursesFromStudent(studentId);
        return ApiResponse.empty();
    }

    @GetMapping("/student-registrations")
    public ApiResponse<PageResponse<RegistrationResponse>> getAllRegistrationsForStudent(
            @RequestParam long studentId, @RequestParam int current, @RequestParam int pageSize) {
        return ApiResponse.success(registrationService.getAllRegistrationsForStudent(studentId, current, pageSize));
    }

    @GetMapping("/course-registrations")
    public ApiResponse<PageResponse<RegistrationResponse>> getAllRegistrationsForCourse(
            @RequestParam long courseId, @RequestParam int current, @RequestParam int pageSize) {
        return ApiResponse.success(registrationService.getAllRegistrationsForCourse(courseId, current, pageSize));
    }

    @GetMapping("/semester-registrations")
    public ApiResponse<PageResponse<RegistrationResponse>> getAllRegistrationsForSemester(
            @RequestParam long semesterId, @RequestParam int current, @RequestParam int pageSize) {
        return ApiResponse.success(registrationService.getAllRegistrationsForSemester(semesterId, current, pageSize));
    }
}
