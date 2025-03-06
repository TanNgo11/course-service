package com.shadcn.courseservice.repository.custom;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import com.shadcn.courseservice.entity.BaseCourse;
import com.shadcn.courseservice.entity.Course;

public interface CustomCourseRepository {
    Page<Course> findByDepartmentIdAndSemesterId(String departmentId, String semesterId, Pageable pageable);

    Page<Course> findUnregisteredCoursesByDepartmentIdAndSemesterIdAndStudentId(
            String departmentId, String semesterId, String studentId, Pageable pageable);

    Page<Course> findRegisteredCoursesByDepartmentIdAndSemesterIdAndStudentId(
            String departmentId, String semesterId, String studentId, Pageable pageable);

    Page<Course> findByDepartmentId(Long departmentId, Pageable pageable);

    List<Course> findByDepartmentIdToList(Long departmentId);

    // remove opening courses from semester by semester id and course ids
    void removeOpeningCoursesFromSemester(Long semesterId, List<Long> courseIds);

    Page<BaseCourse> findOpenCoursesBySemesterAdnDepartment(Long semesterId, Long departmentId, Pageable pageable);
}
