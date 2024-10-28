package com.shadcn.courseservice.mapper;

import java.util.List;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import com.shadcn.courseservice.dto.response.LessonResponse;
import com.shadcn.courseservice.entity.Lesson;

@Mapper(componentModel = "spring")
public interface LessonMapper {

    @Mapping(target = "courseId", source = "course.id")
    LessonResponse toLessonResponse(Lesson lesson);

    List<LessonResponse> toLessonResponseList(List<Lesson> lessons);
}
