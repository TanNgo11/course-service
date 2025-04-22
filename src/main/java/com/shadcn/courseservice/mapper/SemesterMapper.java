package com.shadcn.courseservice.mapper;

import java.util.List;

import org.mapstruct.Mapper;

import com.shadcn.courseservice.dto.response.academicYear.SemesterResponse;
import com.shadcn.courseservice.entity.Semester;

@Mapper(componentModel = "spring")
public interface SemesterMapper {
    List<SemesterResponse> toListSemesterResponse(List<Semester> semesters);

    SemesterResponse toSemesterResponse(Semester semester);
}
