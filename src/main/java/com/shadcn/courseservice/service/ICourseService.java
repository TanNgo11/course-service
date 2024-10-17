package com.shadcn.courseservice.service;

import java.util.List;

import com.shadcn.courseservice.dto.response.CourseResponse;
import com.shadcn.courseservice.dto.response.PageResponse;

public interface ICourseService {
    void addStudentIntoCourse(String departmentId, String courseId, List<String> studentIds);

    void removeStudentFromCourse(String departmentId, String courseId, List<String> studentIds);

    void addTeacherIntoCourse(String departmentId, String courseId, List<String> teacherIds);

    void removeTeacherFromCourse(String departmentId, String courseId, List<String> teacherIds);

    void addSemesterIntoCourse(String departmentId, String courseId, List<String> semesterIds);

    void removeSemesterFromCourse(String departmentId, String courseId, List<String> semesterIds);

    void addTeacherReferenceIntoCourse(String departmentId, String courseId, List<String> teacherIds);

    void removeTeacherReferenceFromCourse(String departmentId, String courseId, List<String> teacherIds);

    void addStudentReferenceIntoCourse(String departmentId, String courseId, List<String> studentIds);

    void removeStudentReferenceFromCourse(String departmentId, String courseId, List<String> studentIds);

    PageResponse<CourseResponse> getAllCourses(Integer current, Integer pageSize);
}
