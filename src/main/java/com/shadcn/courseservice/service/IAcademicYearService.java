package com.shadcn.courseservice.service;

import com.shadcn.courseservice.dto.request.AcademicYearCreation;
import com.shadcn.courseservice.dto.request.AcademicYearUpdation;
import com.shadcn.courseservice.dto.response.AcademicYearResponse;
import com.shadcn.courseservice.dto.response.PageResponse;

public interface IAcademicYearService {
    void createAcademicYear(AcademicYearCreation academicYearCreation);

    void updateAcademicYear(AcademicYearUpdation academicYearUpdation);

    void addDepartmentToAcademicYear(Long academicYearId, Long departmentId);

    void removeDepartmentFromAcademicYear(Long academicYearId, Long departmentId);

    void addSemesterToAcademicYear(Long academicYearId, Long semesterId);

    void removeSemesterFromAcademicYear(Long academicYearId, Long semesterId);

    PageResponse<AcademicYearResponse> getAllAcademicYears(int current, int pageSize);
}
