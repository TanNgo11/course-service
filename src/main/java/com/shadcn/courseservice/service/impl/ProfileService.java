package com.shadcn.courseservice.service.impl;

import java.util.List;

import org.springframework.stereotype.Component;

import com.shadcn.courseservice.dto.response.StudentProfileResponse;
import com.shadcn.courseservice.dto.response.TeacherProfileResponse;
import com.shadcn.courseservice.repository.httpClient.ProfileClient;
import com.shadcn.courseservice.service.IProfileService;

import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;

@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@Component
public class ProfileService implements IProfileService {
    ProfileClient profileClient;

    @Override
    public List<StudentProfileResponse> getPublicStudentProfiles(long[] studentIds) {
        return profileClient.getPublicStudentProfile(studentIds).getResult();
    }

    @Override
    public List<TeacherProfileResponse> getPublicTeacherProfiles(long[] teacherIds) {
        return profileClient.getPublicTeacherProfile(teacherIds).getResult();
    }

    @Override
    public boolean isStudentExist(long studentId) {
        StudentProfileResponse studentProfile = profileClient
                .getPublicStudentProfile(new long[] {studentId})
                .getResult()
                .get(0);
        if (studentProfile == null) {
            return false;
        }
        return true;
    }

    @Override
    public boolean isTeacherExist(long teacherId) {
        TeacherProfileResponse teacherProfile = profileClient
                .getPublicTeacherProfile(new long[] {teacherId})
                .getResult()
                .get(0);
        if (teacherProfile == null) {
            return false;
        }
        return true;
    }
}
