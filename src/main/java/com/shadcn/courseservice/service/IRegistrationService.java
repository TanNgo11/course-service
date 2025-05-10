package com.shadcn.courseservice.service;

import java.util.List;

import com.shadcn.courseservice.dto.request.registration.RegistrationTeacherRoleRequest;
import com.shadcn.courseservice.dto.response.PageResponse;
import com.shadcn.courseservice.dto.response.course.CourseResponse;
import com.shadcn.courseservice.dto.response.registration.RegistrationResponse;
import com.shadcn.courseservice.entity.Registration;

public interface IRegistrationService {
    void registerStudentToCourse(long studentId, List<Long> courseId, long semesterId);

    void registerTeacherToCourse(
            long teacherId, List<Long> courseId, long semesterId, long departmentId, String username);

    void registerTeacherRoleToCourse(RegistrationTeacherRoleRequest request);

    void unregisterTeacherFromCourse(long teacherId, List<Long> courseId, long semesterId);

    void unregisterStudentsFromCourseForStudent(long studentId, List<String> courseCodes, long semesterId);

    void unregisterStudentsFromCourseForAdmin(long courseId, long studentId);

    void unregisterAllCoursesFromStudent(long studentId);

    PageResponse<RegistrationResponse> getAllRegistrationsForStudent(long studentId, int current, int pageSize);

    PageResponse<RegistrationResponse> getAllRegistrationsForCourse(long courseId, int current, int pageSize);

    PageResponse<RegistrationResponse> getAllRegistrationsForSemester(long semesterId, int current, int pageSize);

    void addStudentToCourse(List<Registration> registrations);
    // void addStudentsToCourseInSemester();

    PageResponse<RegistrationResponse> getRegistrationsByStudentIdAndSemesterId(
            long studentId, long semesterId, int current, int pageSize);

    PageResponse<CourseResponse> getAllUnregisteredCoursesInSemesterByDepartmentForStudent(
            String studentId, String semesterId, String departmentId, int current, int pageSize);

    PageResponse<CourseResponse> getAllRegisteredCoursesInSemesterByDepartmentForStudent(
            String studentId, String semesterId, String departmentId, int current, int pageSize);

    void approveStudentRegistration(List<Long> registrationIds, long semesterId);
}
