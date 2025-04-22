package com.shadcn.courseservice.service.impl;

import java.util.List;

import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;

import com.shadcn.courseservice.dto.response.student.StudentProfileResponse;
import com.shadcn.courseservice.dto.response.teacher.TeacherProfileResponse;
import com.shadcn.courseservice.repository.httpClient.ProfileClient;
import com.shadcn.courseservice.service.IProfileService;

import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;

@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@Component
@Service
@Slf4j
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
    public List<TeacherProfileResponse> getTeacherProfilesByEntityIds(long[] teacherIds) {
        log.info("ids: ", teacherIds);
        return profileClient
                .getTeacherProfilesByEntityIds(teacherIds.length == 0 ? new long[] {} : teacherIds)
                .getResult();
    }

    @Override
    public List<StudentProfileResponse> getStudentProfilesByEntityIds(long[] studentIds) {
        return profileClient.getStudentProfilesByEntityIds(studentIds).getResult();
    }

    @Override
    public StudentProfileResponse getStudentProfileByStudentEntityId(long studentId) {
        return profileClient
                .getStudentProfileByStudentEntityId(String.valueOf(studentId))
                .getResult();
    }

    @Override
    public TeacherProfileResponse getTeacherProfileByTeacherEntityId(long teacherId) {
        return profileClient
                .getStudentProfileByTeacherEntityId(String.valueOf(teacherId))
                .getResult();
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
