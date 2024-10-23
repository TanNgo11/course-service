package com.shadcn.courseservice.controller;

import com.shadcn.courseservice.dto.request.LessonsDeleteRequest;
import com.shadcn.courseservice.dto.request.ReferenceDeleteRequest;
import com.shadcn.courseservice.dto.request.TeacherReferenceCreateRequest;
import com.shadcn.courseservice.dto.response.ApiResponse;
import com.shadcn.courseservice.service.impl.ReferenceService;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import static com.shadcn.courseservice.constant.PathConstant.API_V1_REFERENCES;

@RestController
@RequestMapping(API_V1_REFERENCES)
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class ReferenceController {

    private final ReferenceService referenceService;
    @PostMapping("/teachers")
    @PreAuthorize("hasRole('ADMIN')")
    ApiResponse<Void> addATeacherReference(@RequestBody TeacherReferenceCreateRequest request) {
        referenceService.addATeacherReference(request.getTeacherId(), request.getCourseId(), request.getDepartmentId());
        return ApiResponse.empty();
    }

    @DeleteMapping("/teachers")
    @PreAuthorize("hasRole('ADMIN')")
    ApiResponse<Void> deleteTeacherReferences(@RequestBody ReferenceDeleteRequest request) {
        referenceService.deleteTeacherReferences(request.getReferenceIds());
        return ApiResponse.empty();
    }
}
