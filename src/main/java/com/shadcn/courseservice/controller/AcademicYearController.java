package com.shadcn.courseservice.controller;

import static com.shadcn.courseservice.constant.PathConstant.API_V1_ACADEMIC_YEARS;

import java.util.List;

import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import com.shadcn.courseservice.dto.request.academicYear.AcademicYearCreation;
import com.shadcn.courseservice.dto.request.academicYear.AcademicYearDepartmentRequest;
import com.shadcn.courseservice.dto.request.academicYear.AcademicYearSemesterRequest;
import com.shadcn.courseservice.dto.request.academicYear.AcademicYearUpdation;
import com.shadcn.courseservice.dto.response.academicYear.AcademicYearResponse;
import com.shadcn.courseservice.dto.response.ApiResponse;
import com.shadcn.courseservice.dto.response.PageResponse;
import com.shadcn.courseservice.dto.response.academicYear.SemesterResponse;
import com.shadcn.courseservice.service.IAcademicYearService;

import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;

@RestController
@RequestMapping(API_V1_ACADEMIC_YEARS)
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@CrossOrigin(origins = "http://localhost:5173")
public class AcademicYearController {
    IAcademicYearService academicYearService;

    @PostMapping
    @PreAuthorize("hasRole('ADMIN')")
    ApiResponse<Void> createAcademicYear(@RequestBody AcademicYearCreation authenticationCreationRequest) {
        academicYearService.createAcademicYear(authenticationCreationRequest);
        return ApiResponse.empty();
    }

    @GetMapping()
    @PreAuthorize("hasRole('ADMIN')")
    ApiResponse<PageResponse<AcademicYearResponse>> getAcademicYears(
            @RequestParam(defaultValue = "1", required = false) Integer current,
            @RequestParam(defaultValue = "10", required = false) Integer pageSize) {
        return ApiResponse.success(academicYearService.getAcademicYearsWithPagination(current, pageSize));
    }

    @GetMapping("/all")
    @PreAuthorize("hasRole('ADMIN')")
    ApiResponse<List<AcademicYearResponse>> getAllAcademicYears() {
        return ApiResponse.success(academicYearService.getAllAcademicYears());
    }

    @PutMapping
    @PreAuthorize("hasRole('ADMIN')")
    ApiResponse<Void> updateAcademicYear(@RequestBody AcademicYearUpdation academicYearUpdation) {
        academicYearService.updateAcademicYear(academicYearUpdation);
        return ApiResponse.empty();
    }

    @PostMapping("/department")
    @PreAuthorize("hasRole('ADMIN')")
    ApiResponse<Void> addDepartmentToAcademicYear(@RequestBody AcademicYearDepartmentRequest request) {
        academicYearService.addDepartmentToAcademicYear(request.getYearId(), request.getDepartmentIds());
        return ApiResponse.empty();
    }

    @DeleteMapping("/department/{yearId}")
    @PreAuthorize("hasRole('ADMIN')")
    ApiResponse<Void> removeDepartmentFromAcademicYear(
            @PathVariable Long yearId, @RequestBody List<Long> departmentIds) {
        academicYearService.removeDepartmentFromAcademicYear(yearId, departmentIds);
        return ApiResponse.empty();
    }

    // automatically generated when creating a new academic year
    @PostMapping("/semester")
    @PreAuthorize("hasRole('ADMIN')")
    ApiResponse<Void> addSemesterToAcademicYear(@RequestBody AcademicYearSemesterRequest request) {
        academicYearService.addSemesterToAcademicYear(request.getYearId(), request.getSemesterIds());
        return ApiResponse.empty();
    }

    // soft delete here or can not be deleted
    @DeleteMapping("/semester/{yearId}")
    @PreAuthorize("hasRole('ADMIN')")
    ApiResponse<Void> removeSemesterFromAcademicYear(@PathVariable Long yearId, @RequestBody List<Long> semesterIds) {
        academicYearService.removeSemesterFromAcademicYear(yearId, semesterIds);
        return ApiResponse.empty();
    }

    @GetMapping("/semester/{yearId}")
    @PreAuthorize("hasRole('ADMIN')")
    ApiResponse<List<SemesterResponse>> getSemestersByAcademicYear(@PathVariable Long yearId) {
        return ApiResponse.success(academicYearService.getSemestersByAcademicYearId(yearId));
    }
}
