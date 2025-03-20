package com.shadcn.courseservice.controller;

import static com.shadcn.courseservice.constant.PathConstant.API_V1_REGISTRATIONS;

import com.shadcn.courseservice.dto.request.ApproveRegistrationRequest;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import com.shadcn.courseservice.dto.request.AssignTeacherRequest;
import com.shadcn.courseservice.dto.request.RegistrationRequest;
import com.shadcn.courseservice.dto.request.RemoveRegistrationRequest;
import com.shadcn.courseservice.dto.response.ApiResponse;
import com.shadcn.courseservice.dto.response.CourseResponse;
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
    @PreAuthorize("hasRole('ADMIN') or hasRole('STUDENT')")
    public ApiResponse<RegistrationResponse> registerStudentToCourse(@RequestBody RegistrationRequest request) {
        registrationService.registerStudentToCourse(
                request.getStudentId(), request.getCourseIds(), request.getSemesterId());
        return ApiResponse.empty();
    }

    @DeleteMapping("/unregister-student")
    @PreAuthorize("hasRole('ADMIN') or hasRole('STUDENT')")
    public ApiResponse<Void> unregisterStudentFromCourse(@RequestBody RemoveRegistrationRequest request) {
        registrationService.unregisterStudentsFromCourseForStudent(
                request.getStudentId(), request.getCourseCodes(), request.getSemesterId());
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
    @PreAuthorize("hasRole('ADMIN') or hasRole('STUDENT')")
    public ApiResponse<PageResponse<RegistrationResponse>> getAllRegistrationsForStudent(
            @RequestParam long studentId,
            @RequestParam(defaultValue = "1", required = false) Integer current,
            @RequestParam(defaultValue = "10", required = false) Integer pageSize) {
        return ApiResponse.success(registrationService.getAllRegistrationsForStudent(studentId, current, pageSize));
    }

    @GetMapping("/course-registrations")
    @PreAuthorize("hasRole('ADMIN')")
    public ApiResponse<PageResponse<RegistrationResponse>> getAllRegistrationsForCourse(
            @RequestParam long courseId,
            @RequestParam(defaultValue = "1", required = false) Integer current,
            @RequestParam(defaultValue = "10", required = false) Integer pageSize) {
        return ApiResponse.success(registrationService.getAllRegistrationsForCourse(courseId, current, pageSize));
    }

    @GetMapping("/semester-registrations")
    @PreAuthorize("hasRole('ADMIN')")
    public ApiResponse<PageResponse<RegistrationResponse>> getAllRegistrationsForSemester(
            @RequestParam long semesterId,
            @RequestParam(defaultValue = "1", required = false) Integer current,
            @RequestParam(defaultValue = "10", required = false) Integer pageSize) {
        return ApiResponse.success(registrationService.getAllRegistrationsForSemester(semesterId, current, pageSize));
    }

    @GetMapping("/student-semester-registrations/{studentId}")
    @PreAuthorize("hasRole('ADMIN') or hasRole('STUDENT')")
    public ApiResponse<PageResponse<RegistrationResponse>> getAllRegistrationsForStudentAndSemester(
            @PathVariable long studentId,
            @RequestParam long semesterId,
            @RequestParam(defaultValue = "1", required = false) Integer current,
            @RequestParam(defaultValue = "10", required = false) Integer pageSize) {
        return ApiResponse.success(
                registrationService.getRegistrationsByStudentIdAndSemesterId(studentId, semesterId, current, pageSize));
    }

    @GetMapping("/student-registered-courses")
    @PreAuthorize("hasRole('ADMIN') or hasRole('STUDENT')")
    public ApiResponse<PageResponse<CourseResponse>> getAllRegisteredCoursesInSemesterByDepartmentForStudent(
            @RequestParam String studentId,
            @RequestParam String semesterId,
            @RequestParam String departmentId,
            @RequestParam(defaultValue = "1", required = false) Integer current,
            @RequestParam(defaultValue = "10", required = false) Integer pageSize) {
        return ApiResponse.success(registrationService.getAllRegisteredCoursesInSemesterByDepartmentForStudent(
                studentId, semesterId, departmentId, current, pageSize));
    }

    @GetMapping("/student-unregistered-courses")
    @PreAuthorize("hasRole('ADMIN') or hasRole('STUDENT')")
    public ApiResponse<PageResponse<CourseResponse>> getAllUnregisteredCoursesInSemesterByDepartmentForStudent(
            @RequestParam String studentId,
            @RequestParam String semesterId,
            @RequestParam String departmentId,
            @RequestParam(defaultValue = "1", required = false) Integer current,
            @RequestParam(defaultValue = "10", required = false) Integer pageSize) {
        return ApiResponse.success(registrationService.getAllUnregisteredCoursesInSemesterByDepartmentForStudent(
                studentId, semesterId, departmentId, current, pageSize));
    }

    @PostMapping("/assign-teacher")
    @PreAuthorize("hasRole('ADMIN')")
    public ApiResponse<Void> registerTeacherToCourse(@RequestBody AssignTeacherRequest request) {
        registrationService.registerTeacherToCourse(
                request.getTeacherId(), request.getCourseIds(), request.getSemesterId(), request.getDepartmentId());
        return ApiResponse.empty();
    }

    @DeleteMapping("/remove-teacher")
    @PreAuthorize("hasRole('ADMIN')")
    public ApiResponse<Void> unregisterTeacherFromCourse(@RequestBody AssignTeacherRequest request) {
        registrationService.unregisterTeacherFromCourse(
                request.getTeacherId(), request.getCourseIds(), request.getSemesterId());
        return ApiResponse.empty();
    }

    @PostMapping("/approve-registrations")
    @PreAuthorize("hasRole('ADMIN')")
    public ApiResponse<Void> approveStudentRegistration(@RequestBody ApproveRegistrationRequest request) {
        registrationService.approveStudentRegistration(request.getRegistrationIds(), request.getSemesterId());
        return ApiResponse.empty();
    }
}
