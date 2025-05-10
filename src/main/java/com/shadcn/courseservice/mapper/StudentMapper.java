package com.shadcn.courseservice.mapper;

import org.mapstruct.Mapper;

import com.shadcn.courseservice.dto.response.student.StudentReferenceResponse;
import com.shadcn.courseservice.entity.StudentReference;

@Mapper(componentModel = "spring")
public interface StudentMapper {
    StudentReferenceResponse toStudentReferenceResponse(StudentReference studentReference);
}
