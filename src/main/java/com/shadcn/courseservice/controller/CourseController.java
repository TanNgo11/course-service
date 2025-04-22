package com.shadcn.courseservice.controller;

import static com.shadcn.courseservice.constant.PathConstant.API_V1_DEPARTMENTS;

import java.util.List;

import com.shadcn.courseservice.dto.request.course.BaseCourseCreationRequest;
import com.shadcn.courseservice.dto.request.course.CourseAddRequest;
import com.shadcn.courseservice.dto.request.course.CourseRemoveRequest;
import com.shadcn.courseservice.dto.request.file.FileUploadRequest;
import com.shadcn.courseservice.dto.request.file.ListFileUploadRequest;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import com.shadcn.courseservice.dto.request.course.UpdateCourseInformationRequest;
import com.shadcn.courseservice.dto.response.ApiResponse;
import com.shadcn.courseservice.dto.response.PageResponse;
import com.shadcn.courseservice.dto.response.user.UserProfileResponse;
import com.shadcn.courseservice.dto.response.course.BaseCourseResponse;
import com.shadcn.courseservice.dto.response.course.CourseResponse;
import com.shadcn.courseservice.service.ICourseService;

import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;

@RestController
@RequestMapping(API_V1_DEPARTMENTS)
@RequiredArgsConstructor
@Slf4j
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class CourseController {
    ICourseService courseService;

    @PostMapping("/course/add-students")
    @PreAuthorize("hasRole('ADMIN')")
    ApiResponse<Void> addStudentIntoCourse(@RequestBody CourseAddRequest request) {
        courseService.addStudentIntoCourse(request.getDepartmentId(), request.getCourseId(), request.getStudentIds());
        return ApiResponse.success(null);
    }

    @DeleteMapping("/course/remove-students")
    @PreAuthorize("hasRole('ADMIN')")
    ApiResponse<Void> removeStudentFromCourse(@RequestBody CourseRemoveRequest request) {
        courseService.removeStudentFromCourse(
                request.getDepartmentId(), request.getCourseId(), request.getStudentIds());
        return ApiResponse.success(null);
    }

    @PostMapping("/course/add-teachers")
    @PreAuthorize("hasRole('ADMIN')")
    ApiResponse<Void> addTeachersIntoCourse(@RequestBody CourseAddRequest request) {
        courseService.addTeacherIntoCourse(request.getDepartmentId(), request.getCourseId(), request.getTeacherIds());
        return ApiResponse.success(null);
    }

    @DeleteMapping("/course/remove-teachers")
    @PreAuthorize("hasRole('ADMIN')")
    ApiResponse<Void> removeTeachersFromCourse(@RequestBody CourseRemoveRequest request) {
        courseService.removeTeacherFromCourse(
                request.getDepartmentId(), request.getCourseId(), request.getTeacherIds());
        return ApiResponse.success(null);
    }

    @PostMapping("/course/add-semesters")
    @PreAuthorize("hasRole('ADMIN')")
    ApiResponse<Void> addSemestersIntoCourse(@RequestBody CourseAddRequest request) {
        courseService.addSemesterIntoCourse(request.getDepartmentId(), request.getCourseId(), request.getSemesterIds());
        return ApiResponse.success(null);
    }

    @DeleteMapping("/course/remove-semesters")
    @PreAuthorize("hasRole('ADMIN')")
    ApiResponse<Void> removeSemestersFromCourse(@RequestBody CourseRemoveRequest request) {
        courseService.removeSemesterFromCourse(
                request.getDepartmentId(), request.getCourseId(), request.getSemesterIds());
        return ApiResponse.success(null);
    }

    @GetMapping("/course/{courseId}/students")
    @PreAuthorize("hasRole('ADMIN')")
    ApiResponse<PageResponse<UserProfileResponse>> getAllStudentsByIds(
            @PathVariable String courseId,
            @RequestParam(defaultValue = "1", required = false) Integer current,
            @RequestParam(defaultValue = "10", required = false) Integer pageSize) {
        return ApiResponse.success(courseService.getAllStudentsInCourseByIds(courseId, current, pageSize));
    }

    @GetMapping("/course/{courseId}/teachers")
    @PreAuthorize("hasRole('ADMIN')")
    ApiResponse<PageResponse<UserProfileResponse>> getAllTeachersByIds(
            @PathVariable String courseId,
            @RequestParam(defaultValue = "1", required = false) Integer current,
            @RequestParam(defaultValue = "10", required = false) Integer pageSize) {
        return ApiResponse.success(courseService.getAllTeachersInCourseByIds(courseId, current, pageSize));
    }

    @PostMapping(value = "/{departmentId}/course/{courseId}/upload-image")
    @PreAuthorize("hasRole('ADMIN')")
    ApiResponse<Void> uploadCourseImage(
            @PathVariable String departmentId,
            @PathVariable String courseId,
            @ModelAttribute FileUploadRequest fileUploadRequest) {
        log.info("upload image");
        courseService.uploadCourseImage(departmentId, courseId, fileUploadRequest.getFile());
        return ApiResponse.success(null);
    }

    @PostMapping(value = "/{departmentId}/course/{courseId}/upload-file")
    @PreAuthorize("hasRole('ADMIN')")
    ApiResponse<Void> uploadCourseFile(
            @PathVariable String departmentId,
            @PathVariable String courseId,
            @ModelAttribute ListFileUploadRequest listFileUploadRequest) {
        log.info("upload file");
        courseService.uploadCourseFile(departmentId, courseId, listFileUploadRequest.getFiles());
        return ApiResponse.success(null);
    }

    @GetMapping(value = "/courses")
    @PreAuthorize("hasRole('ADMIN')")
    ApiResponse<PageResponse<BaseCourseResponse>> getAllCourses(
            @RequestParam(defaultValue = "1", required = false) Integer current,
            @RequestParam(defaultValue = "10", required = false) Integer pageSize) {
        return ApiResponse.success(courseService.getAllCourses(current, pageSize));
    }

    // Create base course
    @PostMapping(value = "/base-course")
    @PreAuthorize("hasRole('ADMIN')")
    ApiResponse<Void> createBaseCourse(@RequestBody BaseCourseCreationRequest request) {
        courseService.createBaseCourse(request);
        return ApiResponse.success(null);
    }

    @GetMapping(value = "/courses/teacher/semester/{semesterId}")
    @PreAuthorize("hasRole('TEACHER')")
    ApiResponse<List<CourseResponse>> getCoursesByTeacherAndSemester(@PathVariable String semesterId) {
        return ApiResponse.success(courseService.getCoursesOfCurrentTeacherBySemesterId(semesterId));
    }

    @GetMapping(value = "/courses/{courseId}")
    @PreAuthorize("hasRole('TEACHER') or hasRole('ADMIN') or hasRole('STUDENT')")
    ApiResponse<CourseResponse> getCourseById(@PathVariable String courseId) {
        return ApiResponse.success(courseService.getCourseById(courseId));
    }

    @PutMapping(value = "/courses/{courseId}")
    @PreAuthorize("hasRole('ADMIN') or hasRole('TEACHER')")
    ApiResponse<Void> updateCourseInformation(
            @RequestBody UpdateCourseInformationRequest request, @PathVariable Long courseId) {
        courseService.updateCourseInformation(request, courseId);
        return ApiResponse.empty();
    }
}
