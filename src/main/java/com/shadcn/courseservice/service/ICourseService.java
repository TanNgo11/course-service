package com.shadcn.courseservice.service;

import java.util.List;

import org.springframework.web.multipart.MultipartFile;

import com.shadcn.courseservice.dto.request.course.BaseCourseCreationRequest;
import com.shadcn.courseservice.dto.request.course.CourseCreationRequest;
import com.shadcn.courseservice.dto.request.course.UpdateConstraintCourseRequest;
import com.shadcn.courseservice.dto.request.course.UpdateCourseInformationRequest;
import com.shadcn.courseservice.dto.response.*;
import com.shadcn.courseservice.dto.response.course.BaseCourseResponse;
import com.shadcn.courseservice.dto.response.course.CourseResponse;
import com.shadcn.courseservice.dto.response.user.UserProfileResponse;

public interface ICourseService {
    void addStudentIntoCourse(String departmentId, String courseId, List<String> studentIds);

    void removeStudentFromCourse(String departmentId, String courseId, List<String> studentIds);

    void addTeacherIntoCourse(String departmentId, String courseId, List<String> teacherIds);

    void removeTeacherFromCourse(String departmentId, String courseId, List<String> teacherIds);

    void addSemesterIntoCourse(String departmentId, String courseId, List<String> semesterIds);

    void removeSemesterFromCourse(String departmentId, String courseId, List<String> semesterIds);

    PageResponse<UserProfileResponse> getAllStudentsInCourseByIds(String courseId, int current, int pageSize);

    PageResponse<UserProfileResponse> getAllTeachersInCourseByIds(String courseId, int current, int pageSize);

    void uploadCourseImage(String departmentId, String courseId, MultipartFile image);

    void uploadCourseFile(String departmentId, String courseId, List<MultipartFile> files);

    PageResponse<BaseCourseResponse> getAllCourses(Integer current, Integer pageSize);

    void createBaseCourse(BaseCourseCreationRequest request);

    void createNewCourseFromBaseCourseInSemester(CourseCreationRequest request);

    void removeCourseInstanceFromSemester(String courseId, String semesterId);

    //    void addRegistrationToCourseInSemester(Registration registration d);
    //
    //    void registerCourseForStudent(String studentId, list<String> courseId);

    List<CourseResponse> getCoursesOfCurrentTeacherBySemesterId(String semesterId);

    CourseResponse getCourseById(String courseId);

    void updateCourseInformation(UpdateCourseInformationRequest request, Long courseId);

    PageResponse<CourseResponse> findAllCoursesBySemesterId(Long semesterId, int current, int pageSize);

    void updateCourseConstraint(Long id, UpdateConstraintCourseRequest request);
}
