package com.shadcn.courseservice.mapper;

import org.mapstruct.Mapper;

import com.shadcn.courseservice.dto.response.CourseResponse;
import com.shadcn.courseservice.entity.Course;

@Mapper(componentModel = "spring")
public interface CourseMapper {

    // Course toCourse(CourseRequest request);

    CourseResponse toCourseResponse(Course course);
}
