package com.shadcn.courseservice.mapper;

import com.shadcn.courseservice.dto.response.attendance.AttendanceResponse;
import com.shadcn.courseservice.entity.Attendance;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Mappings;

@Mapper(componentModel = "spring")
public interface AttendanceMapper {
    @Mappings({
        @Mapping(target = "studentId", source = "attendance.student.studentId"),
        @Mapping(target = "studentName", source = "attendance.student.name"),
        @Mapping(target = "classSessionId", source = "classSession.id"), @Mapping(target = "status", source = "attendance.status"),
        @Mapping(target = "notes", source = "notes"),
        @Mapping(target = "date", source = "attendance.classSession.sessionDate"),
    })
    AttendanceResponse toAttendanceResponse(Attendance attendance);
}
