package com.shadcn.courseservice.repository.custom;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import com.shadcn.courseservice.entity.BaseCourse;
import com.shadcn.courseservice.entity.Course;

public interface CustomCourseRepository {
    Page<Course> findByDepartmentIdAndSemesterId(String departmentId, String semesterId, Pageable pageable);

    Page<Course> findByDepartmentId(Long departmentId, Pageable pageable);

    Page<BaseCourse> findOpenCoursesBySemesterAdnDepartment(Long semesterId, Long departmentId, Pageable pageable);
}
