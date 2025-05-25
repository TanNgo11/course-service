package com.shadcn.courseservice.mapper;

import java.util.List;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import com.shadcn.courseservice.dto.response.timetable.TimetableResponse;
import com.shadcn.courseservice.entity.Timetable;

@Mapper(
        componentModel = "spring",
        uses = {CourseMapper.class, ClassSessionMapper.class})
public interface TimetableMapper {

    @Mapping(target = "course", qualifiedByName = "toCourseResponse")
    TimetableResponse toTimetableResponse(Timetable timetable);

    List<TimetableResponse> toTimetableResponseList(List<Timetable> lessons);
}
