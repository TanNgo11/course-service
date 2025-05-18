package com.shadcn.courseservice.mapper;


import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Mappings;

import com.shadcn.courseservice.dto.response.attendance.class_session.ClassSessionResponse;
import com.shadcn.courseservice.entity.ClassSession;

@Mapper(
        componentModel = "spring",
        uses = {AttendanceMapper.class})
public interface ClassSessionMapper {

    @Mappings({
        @Mapping(target = "teacherId", source = "classSession.teacher.id"),
        @Mapping(target = "roomId", source = "classSession.room.id"),
        @Mapping(target = "attendances", source = "classSession.attendances"),
    })
    ClassSessionResponse toClassSessionResponse(ClassSession classSession);
}
