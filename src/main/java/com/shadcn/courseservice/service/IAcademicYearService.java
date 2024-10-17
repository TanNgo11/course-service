package com.shadcn.courseservice.service;

import java.util.List;

import com.shadcn.courseservice.dto.request.AcademicYearCreation;
import com.shadcn.courseservice.dto.request.AcademicYearUpdation;
import com.shadcn.courseservice.dto.response.AcademicYearResponse;
import com.shadcn.courseservice.dto.response.PageResponse;

public interface IAcademicYearService {
    void createAcademicYear(AcademicYearCreation academicYearCreation);

    void updateAcademicYear(AcademicYearUpdation academicYearUpdation);

    void addDepartmentToAcademicYear(Long academicYearId, List<Long> departmentId);

    void removeDepartmentFromAcademicYear(Long academicYearId, List<Long> departmentId);

    void addSemesterToAcademicYear(Long academicYearId, List<Long> semesterId);

    void removeSemesterFromAcademicYear(Long academicYearId, List<Long> semesterId);

    PageResponse<AcademicYearResponse> getAllAcademicYears(int current, int pageSize);
}
