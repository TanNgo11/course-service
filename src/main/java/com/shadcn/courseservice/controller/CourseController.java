package com.shadcn.courseservice.controller;

import static com.shadcn.courseservice.constant.PathConstant.API_V1_DEPARTMENTS;

import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import com.shadcn.courseservice.dto.request.CourseAddRequest;
import com.shadcn.courseservice.dto.request.CourseRemoveRequest;
import com.shadcn.courseservice.dto.response.ApiResponse;
import com.shadcn.courseservice.service.ICourseService;

import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;

@RestController
@RequestMapping(API_V1_DEPARTMENTS)
@RequiredArgsConstructor
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

    //    @GetMapping("/course/all-courses")
    //    @PreAuthorize("hasRole('ADMIN')")
    //    ApiResponse<PageResponse<CourseResponse>> getCourses(
    //            @RequestParam(defaultValue = "1", required = false) Integer current,
    //            @RequestParam(defaultValue = "10", required = false) Integer pageSize) {
    //        return ApiResponse.success(courseService.getAllCourses(current, pageSize));
    //    }
}
