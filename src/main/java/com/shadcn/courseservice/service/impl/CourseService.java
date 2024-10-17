package com.shadcn.courseservice.service.impl;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import com.shadcn.courseservice.dto.response.CourseResponse;
import com.shadcn.courseservice.dto.response.PageResponse;
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
    private final CourseMapper courseMapper;

    @Override
    public void addStudentIntoCourse(String departmentId, String courseId, List<String> studentIds) {
        Department department = getDepartment(Long.valueOf(departmentId));
        Course course = getCourse(department, courseId);

        for (String id : studentIds) {
            if (course.getStudentIds().contains(id)) {
                continue;
            }
            course.getStudentIds().add(id);
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
    public void addTeacherIntoCourse(String departmentId, String courseId, List<String> teacherIds) {
        Department department = getDepartment(Long.valueOf(departmentId));
        Course course = getCourse(department, courseId);

        for (String id : teacherIds) {
            if (course.getTeacherIds().contains(id)) {
                continue;
            }
            course.getTeacherIds().add(id);
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
    public void addTeacherReferenceIntoCourse(String departmentId, String courseId, List<String> teacherIds) {
        // TODO
    }

    @Override
    public void removeTeacherReferenceFromCourse(String departmentId, String courseId, List<String> teacherIds) {
        // TODO
    }

    @Override
    public void addStudentReferenceIntoCourse(String departmentId, String courseId, List<String> studentIds) {
        // TODO
    }

    @Override
    public void removeStudentReferenceFromCourse(String departmentId, String courseId, List<String> studentIds) {
        // TODO
    }

    @Override
    public PageResponse<CourseResponse> getAllCourses(Integer current, Integer pageSize) {
        Pageable pageable = PageRequest.of(current - 1, pageSize);
        Page<Course> academicYears = courseRepository.findAll(pageable);

        return ConverToPaginationResponse.toPageResponse(academicYears, courseMapper::toCourseResponse, current);
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
