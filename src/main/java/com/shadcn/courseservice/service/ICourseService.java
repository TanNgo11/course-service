package com.shadcn.courseservice.service;

import java.util.List;

import org.springframework.web.multipart.MultipartFile;

import com.shadcn.courseservice.dto.request.BaseCourseCreationRequest;
import com.shadcn.courseservice.dto.request.CourseCreationRequest;
import com.shadcn.courseservice.dto.response.*;

public interface ICourseService {
    void addStudentIntoCourse(String departmentId, String courseId, List<String> studentIds);

    void removeStudentFromCourse(String departmentId, String courseId, List<String> studentIds);

    void addTeacherIntoCourse(String departmentId, String courseId, List<String> teacherIds);

    void removeTeacherFromCourse(String departmentId, String courseId, List<String> teacherIds);

    void addSemesterIntoCourse(String departmentId, String courseId, List<String> semesterIds);

    void removeSemesterFromCourse(String departmentId, String courseId, List<String> semesterIds);

    PageResponse<StudentProfileResponse> getAllStudentsInCourseByIds(
            String departmentId, String courseId, int current, int pageSize);

    PageResponse<TeacherProfileResponse> getAllTeachersInCourseByIds(
            String departmentId, String courseId, int current, int pageSize);

    void uploadCourseImage(String departmentId, String courseId, MultipartFile image);

    void uploadCourseFile(String departmentId, String courseId, List<MultipartFile> files);

    PageResponse<BaseCourseResponse> getAllCourses(Integer current, Integer pageSize);

    void createBaseCourse(BaseCourseCreationRequest request);

    void createNewCourseFromBaseCourseInSemester(CourseCreationRequest request);

    void removeCourseInstanceFromSemester(String courseId, String semesterId);
}
