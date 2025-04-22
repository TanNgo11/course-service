package com.shadcn.courseservice.service;

import java.util.List;

import com.shadcn.courseservice.dto.response.student.StudentProfileResponse;
import com.shadcn.courseservice.dto.response.teacher.TeacherProfileResponse;

public interface IProfileService {
    List<StudentProfileResponse> getPublicStudentProfiles(long[] studentIds);

    List<TeacherProfileResponse> getPublicTeacherProfiles(long[] teacherIds);

    List<TeacherProfileResponse> getTeacherProfilesByEntityIds(long[] teacherIds);

    List<StudentProfileResponse> getStudentProfilesByEntityIds(long[] studentIds);

    StudentProfileResponse getStudentProfileByStudentEntityId(long studentId);

    TeacherProfileResponse getTeacherProfileByTeacherEntityId(long teacherId);

    boolean isStudentExist(long studentId);

    boolean isTeacherExist(long teacherId);
}
