package com.shadcn.courseservice.controller;

import static com.shadcn.courseservice.constant.PathConstant.*;

import java.util.List;

import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import com.shadcn.courseservice.dto.request.course.CourseIdsRequest;
import com.shadcn.courseservice.dto.response.ApiResponse;
import com.shadcn.courseservice.dto.response.PageResponse;
import com.shadcn.courseservice.dto.response.academicYear.SemesterResponse;
import com.shadcn.courseservice.dto.response.course.CourseResponse;
import com.shadcn.courseservice.service.ISemesterService;

import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;

@RestController
@RequestMapping(API_V1_SEMESTERS)
@RequiredArgsConstructor
@Slf4j
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class SemesterController {
    ISemesterService semesterService;

    @GetMapping
    @PreAuthorize("hasRole('ADMIN') or hasRole('TEACHER') or hasRole('STUDENT')")
    public ApiResponse<PageResponse<SemesterResponse>> getAllSemesters(
            @RequestParam(defaultValue = "1", required = false) Integer current,
            @RequestParam(defaultValue = "10", required = false) Integer pageSize) {
        return ApiResponse.success(semesterService.getAllSemesters(current, pageSize));
    }

    @PutMapping("/open-registration")
    @PreAuthorize("hasRole('ADMIN')")
    public ApiResponse<Void> openRegistrationForSemester(@RequestParam long semesterId) {
        semesterService.openRegistrationForSemester(semesterId);
        return ApiResponse.empty();
    }

    @PutMapping("/close-registration")
    @PreAuthorize("hasRole('ADMIN')")
    public ApiResponse<Void> closeRegistrationForSemester(@RequestParam long semesterId) {
        semesterService.closeRegistrationForSemester(semesterId);
        return ApiResponse.empty();
    }

    @PostMapping("/add-open-courses/{semesterId}")
    @PreAuthorize("hasRole('ADMIN') or hasRole('STUDENT')")
    public ApiResponse<Void> addOpenCoursesToSemester(
            @RequestBody List<Long> baseCourseIds, @PathVariable long semesterId) {
        semesterService.addOpenCoursesToSemester(baseCourseIds, semesterId);
        return ApiResponse.empty();
    }

    @DeleteMapping("/remove-open-courses/{semesterId}")
    @PreAuthorize("hasRole('ADMIN') or hasRole('STUDENT')")
    public ApiResponse<Void> removeOpenCoursesFromSemester(
            @RequestBody List<Long> openingCourseIds, @PathVariable long semesterId) {
        semesterService.removeOpenCoursesFromSemester(openingCourseIds, semesterId);
        return ApiResponse.empty();
    }

    @GetMapping("/open-courses")
    @PreAuthorize("hasRole('ADMIN') or hasRole('STUDENT')")
    public ApiResponse<PageResponse<CourseResponse>> getAllOpenCoursesInSemester(
            @RequestParam String semesterId,
            @RequestParam(defaultValue = "1", required = false) Integer current,
            @RequestParam(defaultValue = "10", required = false) Integer pageSize) {
        return ApiResponse.success(semesterService.getAllOpenCoursesInSemester(semesterId, current, pageSize));
    }

    @GetMapping("/open-courses-department")
    @PreAuthorize("hasRole('ADMIN') or hasRole('STUDENT')")
    public ApiResponse<PageResponse<CourseResponse>> getAllCoursesInSemesterByDepartmentId(
            @RequestParam String semesterId,
            @RequestParam String departmentId,
            @RequestParam(defaultValue = "1", required = false) Integer current,
            @RequestParam(defaultValue = "10", required = false) Integer pageSize) {
        return ApiResponse.success(
                semesterService.getAllCoursesInSemesterByDepartmentId(semesterId, departmentId, current, pageSize));
    }

    @GetMapping("/current-open-semester")
    @PreAuthorize("hasRole('ADMIN') or hasRole('STUDENT')")
    public ApiResponse<SemesterResponse> getCurrentOpenSemester() {
        return ApiResponse.success(semesterService.getCurrentOpenSemester());
    }

    @DeleteMapping("/delete-opened-courses/")
    @PreAuthorize("hasRole('ADMIN')")
    public ApiResponse<Void> deleteCoursesByIds(@RequestBody CourseIdsRequest request) {
        semesterService.deleteCoursesByIds(request);
        return ApiResponse.empty();
    }
}
