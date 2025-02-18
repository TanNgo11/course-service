package com.shadcn.courseservice.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.querydsl.QuerydslPredicateExecutor;
import org.springframework.data.repository.query.Param;

import com.shadcn.courseservice.entity.Course;

public interface CourseRepository extends JpaRepository<Course, Long>, QuerydslPredicateExecutor<Course> {
    // Custom query to find courses by department id
    @Query("SELECT c FROM Course c JOIN c.departments d WHERE d.id = :departmentId")
    Page<Course> findByDepartmentId(@Param("departmentId") Long departmentId, Pageable pageable);

    Page<Course> findBySemesterId(long semesterId, Pageable pageable);
}
