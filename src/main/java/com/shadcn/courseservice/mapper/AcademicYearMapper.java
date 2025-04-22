package com.shadcn.courseservice.mapper;

import java.util.List;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;

import com.shadcn.courseservice.dto.request.academicYear.AcademicYearCreation;
import com.shadcn.courseservice.dto.request.academicYear.AcademicYearUpdation;
import com.shadcn.courseservice.dto.response.academicYear.AcademicYearResponse;
import com.shadcn.courseservice.dto.response.academicYear.SemesterResponse;
import com.shadcn.courseservice.entity.AcademicYear;
import com.shadcn.courseservice.entity.Semester;

@Mapper(componentModel = "spring")
public interface AcademicYearMapper {
    @Mapping(target = "departments", source = "departmentIds", ignore = true)
    AcademicYear toAcademicYear(AcademicYearCreation academicYearCreation);

    void updateAcademicYear(@MappingTarget AcademicYear academicYear, AcademicYearUpdation request);

    AcademicYearResponse toAcademicYearResponse(AcademicYear academicYear);

    @Mapping(target = "departments", source = "departmentIds", ignore = true)
    @Mapping(target = "semesters", source = "semesterIds", ignore = true)
    List<AcademicYearResponse> toListAcademicYearResponse(List<AcademicYear> academicYears);

    List<SemesterResponse> toListSemesterResponse(List<Semester> semesters);
}
