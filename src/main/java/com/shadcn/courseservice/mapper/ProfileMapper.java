package com.shadcn.courseservice.mapper;

import org.mapstruct.Mapper;

import com.shadcn.courseservice.dto.response.AdminProfileResponse;
import com.shadcn.courseservice.dto.response.StudentProfileResponse;
import com.shadcn.courseservice.dto.response.TeacherProfileResponse;
import com.shadcn.courseservice.entity.AdminProfile;
import com.shadcn.courseservice.entity.StudentProfile;
import com.shadcn.courseservice.entity.TeacherProfile;

@Mapper(componentModel = "spring")
public interface ProfileMapper {
    StudentProfileResponse toStudentProfileResponse(StudentProfile studentProfile);

    TeacherProfileResponse toTeacherProfileResponse(TeacherProfile teacherProfile);

    AdminProfileResponse toAdminProfileResponse(AdminProfile adminProfile);

    StudentProfile toStudentProfile(StudentProfileResponse studentProfileResponse);

    TeacherProfile toTeacherProfile(TeacherProfileResponse teacherProfileResponse);
}
