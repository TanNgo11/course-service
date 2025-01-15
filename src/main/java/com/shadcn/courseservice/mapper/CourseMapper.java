package com.shadcn.courseservice.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import com.shadcn.courseservice.dto.request.BaseCourseCreationRequest;
import com.shadcn.courseservice.dto.request.CourseCreationRequest;
import com.shadcn.courseservice.dto.response.BaseCourseResponse;
import com.shadcn.courseservice.dto.response.CourseResponse;
import com.shadcn.courseservice.entity.BaseCourse;
import com.shadcn.courseservice.entity.Course;

@Mapper(componentModel = "spring")
public interface CourseMapper {
    // Course toCourse(CourseRequest request);
    CourseResponse toCourseResponse(Course course);

    BaseCourseResponse toBaseCourseResponse(BaseCourse baseCourse);

    BaseCourse toBaseCourse(BaseCourseCreationRequest baseCourseCreationRequest);

    @Mapping(target = "baseCourse", source = "baseCourse")
    Course toCourse(CourseCreationRequest request, BaseCourse baseCourse);
}
