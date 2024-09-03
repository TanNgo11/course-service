package com.shadcn.courseservice.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;

import com.shadcn.courseservice.dto.request.AcademicYearCreation;
import com.shadcn.courseservice.dto.request.AcademicYearUpdation;
import com.shadcn.courseservice.dto.response.AcademicYearResponse;
import com.shadcn.courseservice.entity.AcademicYear;

@Mapper(componentModel = "spring")
public interface AcademicYearMapper {
    @Mapping(target = "departments", source = "departmentIds", ignore = true)
    @Mapping(target = "semesters", source = "semesterIds", ignore = true)
    AcademicYear toAcademicYear(AcademicYearCreation academicYearCreation);

    void updateAcademicYear(@MappingTarget AcademicYear academicYear, AcademicYearUpdation request);

    AcademicYearResponse toAcademicYearResponse(AcademicYear academicYear);
}
