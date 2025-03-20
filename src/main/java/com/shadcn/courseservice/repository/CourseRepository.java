package com.shadcn.courseservice.repository;

import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.querydsl.QuerydslPredicateExecutor;

import com.shadcn.courseservice.entity.Course;
import com.shadcn.courseservice.repository.custom.CustomCourseRepository;

import feign.Param;

public interface CourseRepository
        extends JpaRepository<Course, Long>, QuerydslPredicateExecutor<Course>, CustomCourseRepository {
    //    // Custom query to find courses by department id
    //    @Query("SELECT c FROM Course c JOIN c.baseCourse.departments d WHERE d.id = :departmentId")
    //    Page<Course> findByDepartmentId(@Param("departmentId") Long departmentId, Pageable pageable);
    //
    //    // find by semesterid and department id
    //    @Query(
    //            "SELECT c FROM Course c JOIN c.baseCourse.departments d WHERE d.id = :departmentId AND c.semester.id =
    // :semesterId")
    //    Page<Course> findByDepartmentIdAndSemesterId(
    //            @Param("departmentId") Long departmentId, @Param("semesterId") Long semesterId, Pageable pageable);

    Page<Course> findBySemesterId(long semesterId, Pageable pageable);

    void removeCourseById(Long id);

    Optional<Course> getCourseById(@Param("id") Long id);

    @Query("SELECT c.teacherReferences FROM Course c WHERE c.id = :courseId")
    long[] findAllTeacherIdsByCourseId(@Param("courseId") Long courseId);

    @Query("SELECT c.studentReferences FROM Course c WHERE c.id = :courseId")
    long[] findAllStudentIdsByCourseId(@Param("courseId") Long courseId);
}
