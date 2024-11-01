package com.shadcn.courseservice.service.impl;

import java.util.Arrays;
import java.util.List;
import java.util.function.Function;
import java.util.stream.Collectors;

import jakarta.annotation.Nullable;
import jakarta.transaction.Transactional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.shadcn.courseservice.dto.response.CourseResponse;
import com.shadcn.courseservice.dto.response.PageResponse;
import com.shadcn.courseservice.dto.response.StudentProfileResponse;
import com.shadcn.courseservice.dto.response.TeacherProfileResponse;
import com.shadcn.courseservice.entity.Course;
import com.shadcn.courseservice.entity.Department;
import com.shadcn.courseservice.entity.Semester;
import com.shadcn.courseservice.exception.AppException;
import com.shadcn.courseservice.exception.ErrorCode;
import com.shadcn.courseservice.mapper.CourseMapper;
import com.shadcn.courseservice.repository.CourseRepository;
import com.shadcn.courseservice.repository.DepartmentRepository;
import com.shadcn.courseservice.repository.SemesterRepository;
import com.shadcn.courseservice.service.ICourseService;
import com.shadcn.courseservice.service.IFileUploadService;
import com.shadcn.courseservice.service.IProfileService;
import com.shadcn.courseservice.util.ConverToPaginationResponse;

import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;

@Service
@RequiredArgsConstructor
@Slf4j
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class CourseService implements ICourseService {
    DepartmentRepository departmentRepository;
    CourseRepository courseRepository;
    SemesterRepository semesterRepository;
    CourseMapper courseMapper;
    IFileUploadService fileUploadService;
    IProfileService profileService;

    @Override
    public void addStudentIntoCourse(
            String departmentId,
            String courseId,
            List<String> studentIds) {
        Department department = getDepartment(Long.valueOf(departmentId));
        Course course = getCourse(department, courseId);

        for (String id : studentIds) {
            if (!course.getStudentIds().contains(id)) {
                course.getStudentIds().add(id);
            }
        }

        departmentRepository.save(department);
        courseRepository.save(course);
    }

    @Override
    public void removeStudentFromCourse(String departmentId, String courseId, List<String> studentIds) {
        Department department = getDepartment(Long.valueOf(departmentId));
        Course course = getCourse(department, courseId);

        for (String id : studentIds) {
            if (!course.getStudentIds().remove(id)) {
                throw new AppException(ErrorCode.STUDENT_NOT_FOUND);
            }
        }

        departmentRepository.save(department);
        courseRepository.save(course);
    }

    @Override
    public void addTeacherIntoCourse(
            String departmentId,
            String courseId,
            List<String> teacherIds) {
        Department department = getDepartment(Long.valueOf(departmentId));
        Course course = getCourse(department, courseId);

        for (String id : teacherIds) {
            if (!course.getTeacherIds().contains(id)) {
                course.getTeacherIds().add(id);
            }
        }

        departmentRepository.save(department);
        courseRepository.save(course);
    }

    @Override
    public void removeTeacherFromCourse(String departmentId, String courseId, List<String> teacherIds) {
        Department department = getDepartment(Long.valueOf(departmentId));
        Course course = getCourse(department, courseId);

        for (String id : teacherIds) {
            if (!course.getTeacherIds().remove(id)) {
                throw new AppException(ErrorCode.TEACHER_NOT_FOUND);
            }
        }

        departmentRepository.save(department);
        courseRepository.save(course);
    }

    @Override
    public void addSemesterIntoCourse(String departmentId, String courseId, List<String> semesterIds) {
        Department department = getDepartment(Long.valueOf(departmentId));
        Course course = getCourse(department, courseId);
        Semester semester;

        for (String id : semesterIds) {
            semester = getSemester(Long.valueOf(id));
            if (course.getSemesters().contains(semester)) {
                continue;
            }
            course.getSemesters().add(semester);
        }

        departmentRepository.save(department);
        courseRepository.save(course);
    }

    @Override
    public void removeSemesterFromCourse(String departmentId, String courseId, List<String> semesterIds) {
        Department department = getDepartment(Long.valueOf(departmentId));
        Course course = getCourse(department, courseId);
        Semester semester;

        for (String id : semesterIds) {
            semester = getSemester(Long.valueOf(id));
            course.getSemesters().remove(semester);
        }

        departmentRepository.save(department);
        courseRepository.save(course);
    }

    @Override
    public PageResponse<StudentProfileResponse> getAllStudentsInCourseByIds(String departmentId, String courseId, int current, int pageSize) {
        Pageable pageable = PageRequest.of(current - 1, pageSize);
        Department department = getDepartment(Long.valueOf(departmentId));
        Course course = getCourse(department, courseId);

        long[] studentIdsArray = course.getStudentIds().stream().mapToLong(Long::valueOf).toArray();
        List<StudentProfileResponse> teacherProfiles = profileService.getPublicStudentProfiles(studentIdsArray);
        Page<StudentProfileResponse> responses = new PageImpl<>(teacherProfiles, pageable, teacherProfiles.size());

        return ConverToPaginationResponse.toPageResponse(responses, Function.identity(), current);
    }

    @Override
    public PageResponse<TeacherProfileResponse> getAllTeachersInCourseByIds(String departmentId, String courseId, int current, int pageSize) {
        Pageable pageable = PageRequest.of(current - 1, pageSize);
        Department department = getDepartment(Long.valueOf(departmentId));
        Course course = getCourse(department, courseId);

        long[] teacherIdsArray = course.getTeacherIds().stream().mapToLong(Long::valueOf).toArray();
        List<TeacherProfileResponse> teacherProfiles = profileService.getPublicTeacherProfiles(teacherIdsArray);
        Page<TeacherProfileResponse> responses = new PageImpl<>(teacherProfiles, pageable, teacherProfiles.size());

        return ConverToPaginationResponse.toPageResponse(responses, Function.identity(), current);
    }

    @Override
    @Transactional
    public void uploadCourseImage(String departmentId, String courseId, MultipartFile image) {
        Course course = getCourse(getDepartment(Long.valueOf(departmentId)), courseId);

        String imageUri = fileUploadService.uploadImageIfPresent(image);

        if (imageUri != null) {
            course.setImageUri(imageUri);
            courseRepository.save(course);
        } else {
            throw new AppException(ErrorCode.IMAGE_UPLOAD_FAILED);
        }

        courseRepository.save(course);
    }

    public Department getDepartment(Long departmentId) {
        return departmentRepository
                .findById(departmentId)
                .orElseThrow(() -> new AppException(ErrorCode.DEPARTMENT_NOT_FOUND));
    }

    public Course getCourse(Department department, String courseId) {
        return department.getCourses().stream()
                .filter(c -> c.getId().equals(Long.valueOf(courseId)))
                .findFirst()
                .orElseThrow(() -> new AppException(ErrorCode.COURSE_NOT_FOUND));
    }

    Semester getSemester(Long semesterId) {
        return semesterRepository
                .findById(semesterId)
                .orElseThrow(() -> new AppException(ErrorCode.SEMESTER_NOT_FOUND));
    }
}
