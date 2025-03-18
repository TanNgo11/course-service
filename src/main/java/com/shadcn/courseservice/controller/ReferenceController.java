package com.shadcn.courseservice.controller;

import static com.shadcn.courseservice.constant.PathConstant.API_V1_REFERENCES;

import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import com.shadcn.courseservice.dto.request.ReferenceDeleteRequest;
import com.shadcn.courseservice.dto.request.StudentReferenceCreateRequest;
import com.shadcn.courseservice.dto.request.TeacherReferenceCreateRequest;
import com.shadcn.courseservice.dto.response.ApiResponse;
import com.shadcn.courseservice.service.impl.ReferenceService;

import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;

@RestController
@RequestMapping(API_V1_REFERENCES)
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class ReferenceController {

    ReferenceService referenceService;

    @PostMapping("/teachers")
    @PreAuthorize("hasRole('ADMIN')")
    ApiResponse<Void> addATeacherReference(@RequestBody TeacherReferenceCreateRequest request) {
        referenceService.addATeacherReference(request.getTeacherId(), request.getDepartmentId());
        return ApiResponse.empty();
    }

    @DeleteMapping("/teachers")
    @PreAuthorize("hasRole('ADMIN')")
    ApiResponse<Void> deleteTeacherReferences(@RequestBody ReferenceDeleteRequest request) {
        referenceService.deleteTeacherReferences(request.getReferenceIds());
        return ApiResponse.empty();
    }

    @PostMapping("/students")
    @PreAuthorize("hasRole('ADMIN')")
    ApiResponse<Void> addAStudentReference(@RequestBody StudentReferenceCreateRequest request) {
        referenceService.addAStudentReference(
                request.getStudentId(), request.getAcademicYearId(), request.getDepartmentId(), request.getName());
        return ApiResponse.empty();
    }

    @DeleteMapping("/students")
    ApiResponse<Void> deleteStudentReferences(@RequestBody ReferenceDeleteRequest request) {
        referenceService.deleteStudentReferences(request.getReferenceIds());
        return ApiResponse.empty();
    }
}
