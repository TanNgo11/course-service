package com.shadcn.courseservice.service;

import java.util.List;

import com.shadcn.courseservice.dto.request.academicYear.AcademicYearCreation;
import com.shadcn.courseservice.dto.request.academicYear.AcademicYearUpdation;
import com.shadcn.courseservice.dto.response.academicYear.AcademicYearResponse;
import com.shadcn.courseservice.dto.response.PageResponse;
import com.shadcn.courseservice.dto.response.academicYear.SemesterResponse;

public interface IAcademicYearService {
    void createAcademicYear(AcademicYearCreation academicYearCreation);

    void updateAcademicYear(AcademicYearUpdation academicYearUpdation);

    void addDepartmentToAcademicYear(Long academicYearId, List<Long> departmentId);

    void removeDepartmentFromAcademicYear(Long academicYearId, List<Long> departmentId);

    void addSemesterToAcademicYear(Long academicYearId, List<Long> semesterId);

    void removeSemesterFromAcademicYear(Long academicYearId, List<Long> semesterId);

    PageResponse<AcademicYearResponse> getAcademicYearsWithPagination(int current, int pageSize);

    List<AcademicYearResponse> getAllAcademicYears();

    List<SemesterResponse> getSemestersByAcademicYearId(Long academicYearId);
}
