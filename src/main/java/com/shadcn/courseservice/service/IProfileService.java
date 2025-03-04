package com.shadcn.courseservice.service;

import java.util.List;

import com.shadcn.courseservice.dto.response.StudentProfileResponse;
import com.shadcn.courseservice.dto.response.TeacherProfileResponse;

public interface IProfileService {
    List<StudentProfileResponse> getPublicStudentProfiles(long[] studentIds);

    List<TeacherProfileResponse> getPublicTeacherProfiles(long[] teacherIds);

    StudentProfileResponse getStudentProfileByStudentEntityId(long studentId);

    boolean isStudentExist(long studentId);

    boolean isTeacherExist(long teacherId);
}
