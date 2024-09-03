package com.shadcn.courseservice.service;

import com.shadcn.courseservice.dto.request.AcademicYearCreation;
import com.shadcn.courseservice.dto.request.AcademicYearUpdation;
import com.shadcn.courseservice.dto.response.AcademicYearResponse;
import com.shadcn.courseservice.dto.response.PageResponse;

public interface IAcademicYearService {
    void createAcademicYear(AcademicYearCreation academicYearCreation);

    void updateAcademicYear(AcademicYearUpdation academicYearUpdation);

    PageResponse<AcademicYearResponse> getAllAcademicYears(int current, int pageSize);
}
