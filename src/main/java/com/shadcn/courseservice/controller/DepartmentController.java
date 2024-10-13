package com.shadcn.courseservice.controller;

import static com.shadcn.courseservice.constant.PathConstant.API_V1_DEPARTMENTS;

import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import com.shadcn.courseservice.dto.request.*;
import com.shadcn.courseservice.dto.response.ApiResponse;
import com.shadcn.courseservice.service.IDepartmentService;

import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;

@RestController
@RequestMapping(API_V1_DEPARTMENTS)
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class DepartmentController {
    IDepartmentService departmentService;

    @PostMapping("/subjects")
    @PreAuthorize("hasRole('ADMIN')")
    ApiResponse<Void> addSubjectsToDepartment(@RequestBody DepartmentSubjectRequest request) {
        departmentService.addSubjectsToDepartment(request.getDepartmentId(), request.getSubjectIds());
        return ApiResponse.empty();
    }

    @DeleteMapping("/subjects")
    @PreAuthorize("hasRole('ADMIN')")
    ApiResponse<Void> removeSubjectsFromDepartment(@RequestBody DepartmentSubjectRequest request) {
        departmentService.removeSubjectsFromDepartment(request.getDepartmentId(), request.getSubjectIds());
        return ApiResponse.empty();
    }
}
