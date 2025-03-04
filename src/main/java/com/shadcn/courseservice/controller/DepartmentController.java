package com.shadcn.courseservice.controller;

import static com.shadcn.courseservice.constant.PathConstant.API_V1_DEPARTMENTS;

import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import com.shadcn.courseservice.dto.request.*;
import com.shadcn.courseservice.dto.response.*;
import com.shadcn.courseservice.service.IDepartmentService;

import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;

@RestController
@RequestMapping(API_V1_DEPARTMENTS)
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@CrossOrigin(origins = "http://localhost:5173")
public class DepartmentController {
    IDepartmentService departmentService;

    @PostMapping("/courses")
    @PreAuthorize("hasRole('ADMIN')")
    ApiResponse<Void> addCoursesToDepartment(@RequestBody DepartmentCourseRequest request) {
        departmentService.addCoursesToDepartment(request.getDepartmentId(), request.getCourseIds());
        return ApiResponse.empty();
    }

    @PostMapping("/base-courses")
    @PreAuthorize("hasRole('ADMIN')")
    ApiResponse<Void> addBaseCoursesToDepartment(@RequestBody DepartmentCourseRequest request) {
        departmentService.addBaseCoursesToDepartment(request.getDepartmentId(), request.getCourseIds());
        return ApiResponse.empty();
    }

    //    @DeleteMapping("/courses")
    //    @PreAuthorize("hasRole('ADMIN')")
    //    ApiResponse<Void> removeCoursesFromDepartment(@RequestBody DepartmentCourseRequest request) {
    //        departmentService.removeCoursesFromDepartment(request.getDepartmentId(), request.getCourseIds());
    //        return ApiResponse.empty();
    //    }

    @DeleteMapping("/base-courses")
    @PreAuthorize("hasRole('ADMIN')")
    ApiResponse<Void> removeBaseCoursesFromDepartment(@RequestBody DepartmentCourseRequest request) {
        departmentService.removeBaseCoursesFromDepartment(request.getDepartmentId(), request.getCourseIds());
        return ApiResponse.empty();
    }

    @GetMapping("/{departmentId}/courses")
    @PreAuthorize("hasRole('ADMIN')")
    ApiResponse<PageResponse<CourseResponse>> getAllCoursesInDepartment(
            @PathVariable Long departmentId,
            @RequestParam(defaultValue = "1", required = false) Integer current,
            @RequestParam(defaultValue = "10", required = false) Integer pageSize) {
        return ApiResponse.success(departmentService.getCoursesByDepartment(departmentId, current, pageSize));
    }

    @GetMapping("/{departmentId}/base-courses")
    @PreAuthorize("hasRole('ADMIN')")
    ApiResponse<PageResponse<BaseCourseResponse>> getAllBaseCoursesInDepartment(
            @PathVariable Long departmentId,
            @RequestParam(defaultValue = "1", required = false) Integer current,
            @RequestParam(defaultValue = "10", required = false) Integer pageSize) {
        return ApiResponse.success(departmentService.getBaseCoursesByDepartment(departmentId, current, pageSize));
    }

    @GetMapping("/unopened-base-courses")
    @PreAuthorize("hasRole('ADMIN')")
    ApiResponse<PageResponse<BaseCourseResponse>> getAllUnOpenedBaseCoursesInDepartment(
            @RequestParam Long semesterId,
            @RequestParam Long departmentId,
            @RequestParam(defaultValue = "1", required = false) Integer current,
            @RequestParam(defaultValue = "10", required = false) Integer pageSize) {
        return ApiResponse.success(
                departmentService.getAllUnOpenedBaseCoursesByDepartment(semesterId, departmentId, current, pageSize));
    }

    @GetMapping
    @PreAuthorize("hasRole('ADMIN')")
    ApiResponse<PageResponse<DepartmentResponse>> getAllDepartments(
            @RequestParam(defaultValue = "1", required = false) Integer current,
            @RequestParam(defaultValue = "10", required = false) Integer pageSize) {
        return ApiResponse.success(departmentService.getAllDepartments(current, pageSize));
    }
}
