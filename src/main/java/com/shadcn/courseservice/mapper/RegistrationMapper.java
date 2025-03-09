package com.shadcn.courseservice.mapper;

import java.util.List;

import org.mapstruct.*;
import org.mapstruct.Mapper;

import com.shadcn.courseservice.dto.response.RegistrationResponse;
import com.shadcn.courseservice.entity.Registration;

@Mapper(componentModel = "spring")
public interface RegistrationMapper {

    @Mappings({
        @Mapping(target = "studentId", source = "registration.studentProfile.studentId"),
        @Mapping(target = "courseName", source = "registration.course.baseCourse.name"),
        @Mapping(target = "courseDescription", source = "registration.course.baseCourse.description"),
        @Mapping(target = "courseCode", source = "registration.course.baseCourse.code"),
        @Mapping(target = "semesterId", source = "registration.semester.id"),
        @Mapping(target = "semesterName", source = "registration.semester.name"),
        @Mapping(target = "thumbnail", source = "registration.course.baseCourse.imageUri"),
    })
    RegistrationResponse toRegistrationResponse(Registration registration);

    List<RegistrationResponse> toRegistrationResponseList(List<Registration> registrations);
}
