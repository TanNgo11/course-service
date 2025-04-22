package com.shadcn.courseservice.mapper;

import java.util.List;

import org.mapstruct.*;
import org.springframework.data.repository.query.Param;

import com.shadcn.courseservice.dto.request.course.BaseCourseCreationRequest;
import com.shadcn.courseservice.dto.request.course.CourseCreationRequest;
import com.shadcn.courseservice.dto.request.course.UpdateCourseInformationRequest;
import com.shadcn.courseservice.dto.response.teacher.TeacherInformationDTO;
import com.shadcn.courseservice.dto.response.course.BaseCourseResponse;
import com.shadcn.courseservice.dto.response.course.CourseResponse;
import com.shadcn.courseservice.entity.BaseCourse;
import com.shadcn.courseservice.entity.Course;

@Mapper(componentModel = "spring")
public interface CourseMapper {
    // Course toCourse(CourseRequest request);
    @Mappings({
        @Mapping(target = "name", source = "baseCourse.name"),
        @Mapping(target = "imageUri", source = "baseCourse.imageUri"),
        @Mapping(target = "code", source = "baseCourse.code"),
        @Mapping(target = "credit", source = "baseCourse.credit"),
        @Mapping(target = "description", source = "baseCourse.description"),
    })
    @Named("toCourseResponse")
    CourseResponse toCourseResponse(@Param("course") Course course);

    @Mappings({
        @Mapping(target = "id", source = "course.id"),
        @Mapping(target = "name", source = "course.baseCourse.name"),
        @Mapping(target = "imageUri", source = "course.baseCourse.imageUri"),
        @Mapping(target = "code", source = "course.baseCourse.code"),
        @Mapping(target = "credit", source = "course.baseCourse.credit"),
        @Mapping(target = "description", source = "course.baseCourse.description"),
        @Mapping(target = "teacher", expression = "java(teacher)")
    })
    @Named("toCourseResponseDetail")
    CourseResponse toCourseResponseDetail(Course course, TeacherInformationDTO teacher);

    BaseCourseResponse toBaseCourseResponse(BaseCourse baseCourse);

    BaseCourse toBaseCourse(BaseCourseCreationRequest baseCourseCreationRequest);

    @Mapping(target = "baseCourse", source = "baseCourse")
    Course toCourse(CourseCreationRequest request, BaseCourse baseCourse);

    @IterableMapping(qualifiedByName = "toCourseResponse")
    List<CourseResponse> toCourseResponseList(List<Course> courses);

    void updateCourseInformation(@MappingTarget Course course, UpdateCourseInformationRequest request);
}
