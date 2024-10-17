package com.shadcn.courseservice.service.impl;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import com.shadcn.courseservice.dto.response.CourseResponse;
import com.shadcn.courseservice.dto.response.PageResponse;
import com.shadcn.courseservice.entity.Course;
import com.shadcn.courseservice.entity.Department;
import com.shadcn.courseservice.exception.AppException;
import com.shadcn.courseservice.exception.ErrorCode;
import com.shadcn.courseservice.mapper.CourseMapper;
import com.shadcn.courseservice.repository.CourseRepository;
import com.shadcn.courseservice.repository.DepartmentRepository;
import com.shadcn.courseservice.service.IDepartmentService;
import com.shadcn.courseservice.util.ConverToPaginationResponse;

import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;

@Service
@RequiredArgsConstructor
@Slf4j
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class DepartmentService implements IDepartmentService {
    DepartmentRepository departmentRepository;
    CourseRepository courseRepository;
    CourseMapper courseMapper;

    @Override
    public void addCoursesToDepartment(Long departmentId, List<Long> courseIds) {
        Department department = getDepartment(departmentId);

        for (Long courseId : courseIds) {
            Course course = getCourse(courseId);
            if (department.getCourses().contains(course)) {
                throw new AppException(ErrorCode.COURSE_EXISTED);
            }
            department.getCourses().add(course);
        }

        departmentRepository.save(department);
    }

    @Override
    public void removeCoursesFromDepartment(Long departmentId, List<Long> courseIds) {
        Department department = getDepartment(departmentId);
        for (Long courseId : courseIds) {
            Course course = getCourse(courseId);
            department.getCourses().remove(course);
        }
        departmentRepository.save(department);
    }

    @Override
    public PageResponse<CourseResponse> getCoursesByDepartment(Long departmentId, Integer current, Integer pageSize) {
        Department department = getDepartment(departmentId);

        Pageable pageable = PageRequest.of(current - 1, pageSize);

        Page<Course> coursePage = new PageImpl<>(
                department.getCourses(), pageable, department.getCourses().size());

        return ConverToPaginationResponse.toPageResponse(coursePage, courseMapper::toCourseResponse, current);
    }

    Department getDepartment(Long departmentId) {
        return departmentRepository
                .findById(departmentId)
                .orElseThrow(() -> new AppException(ErrorCode.DEPARTMENT_NOT_FOUND));
    }

    Course getCourse(Long courseId) {
        return courseRepository.findById(courseId).orElseThrow(() -> new AppException(ErrorCode.COURSE_NOT_FOUND));
    }
}
