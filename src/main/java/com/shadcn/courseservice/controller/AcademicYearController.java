package com.shadcn.courseservice.controller;

import static com.shadcn.courseservice.constant.PathConstant.API_V1_ACADEMIC_YEARS;

import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import com.shadcn.courseservice.dto.request.AcademicYearCreation;
import com.shadcn.courseservice.dto.request.AcademicYearDepartmentRequest;
import com.shadcn.courseservice.dto.request.AcademicYearSemesterRequest;
import com.shadcn.courseservice.dto.request.AcademicYearUpdation;
import com.shadcn.courseservice.dto.response.AcademicYearResponse;
import com.shadcn.courseservice.dto.response.ApiResponse;
import com.shadcn.courseservice.dto.response.PageResponse;
import com.shadcn.courseservice.service.IAcademicYearService;

import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;

@RestController
@RequestMapping(API_V1_ACADEMIC_YEARS)
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class AcademicYearController {
    IAcademicYearService academicYearService;

    @PostMapping
    @PreAuthorize("hasRole('ADMIN')")
    ApiResponse<Void> createAcademicYear(@RequestBody AcademicYearCreation authenticationCreationRequest) {
        academicYearService.createAcademicYear(authenticationCreationRequest);
        return ApiResponse.empty();
    }

    @GetMapping
    @PreAuthorize("hasRole('ADMIN')")
    ApiResponse<PageResponse<AcademicYearResponse>> getAcademicYears(
            @RequestParam(defaultValue = "1", required = false) Integer current,
            @RequestParam(defaultValue = "10", required = false) Integer pageSize) {
        return ApiResponse.success(academicYearService.getAllAcademicYears(current, pageSize));
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
        academicYearService.addDepartmentToAcademicYear(request.getYearId(), request.getDepartmentId());
        return ApiResponse.empty();
    }

    @DeleteMapping("/department/{yearId}/{departmentId}")
    @PreAuthorize("hasRole('ADMIN')")
    ApiResponse<Void> removeDepartmentFromAcademicYear(@PathVariable Long yearId, @PathVariable Long departmentId) {
        academicYearService.removeDepartmentFromAcademicYear(yearId, departmentId);
        return ApiResponse.empty();
    }

    // automatically generated when creating a new academic year
    @PostMapping("/semester")
    @PreAuthorize("hasRole('ADMIN')")
    ApiResponse<Void> addSemesterToAcademicYear(@RequestBody AcademicYearSemesterRequest request) {
        academicYearService.addSemesterToAcademicYear(request.getYearId(), request.getSemesterId());
        return ApiResponse.empty();
    }
    // soft delete here or can not be deleted
    @DeleteMapping("/semester/{yearId}/{semesterId}")
    @PreAuthorize("hasRole('ADMIN')")
    ApiResponse<Void> removeSemesterFromAcademicYear(@PathVariable Long yearId, @PathVariable Long semesterId) {
        academicYearService.removeSemesterFromAcademicYear(yearId, semesterId);
        return ApiResponse.empty();
    }
}
