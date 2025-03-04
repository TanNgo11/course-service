package com.shadcn.courseservice.service;

import java.util.List;

import com.shadcn.courseservice.dto.response.PageResponse;
import com.shadcn.courseservice.dto.response.RegistrationResponse;
import com.shadcn.courseservice.entity.Registration;

public interface IRegistrationService {
    void registerStudentToCourse(long studentId, List<Long> courseId, long semesterId);

    void unregisterStudentsFromCourseForStudent(long courseId, long studentId);

    void unregisterStudentsFromCourseForAdmin(long courseId, long studentId);

    void unregisterAllCoursesFromStudent(long studentId);

    PageResponse<RegistrationResponse> getAllRegistrationsForStudent(long studentId, int current, int pageSize);

    PageResponse<RegistrationResponse> getAllRegistrationsForCourse(long courseId, int current, int pageSize);

    PageResponse<RegistrationResponse> getAllRegistrationsForSemester(long semesterId, int current, int pageSize);

    void addStudentToCourse(List<Registration> registrations);
    // void addStudentsToCourseInSemester();
}
