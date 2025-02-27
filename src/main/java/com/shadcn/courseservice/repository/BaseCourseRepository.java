package com.shadcn.courseservice.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.querydsl.QuerydslPredicateExecutor;
import org.springframework.data.repository.query.Param;

import com.shadcn.courseservice.entity.BaseCourse;

public interface BaseCourseRepository extends JpaRepository<BaseCourse, Long>, QuerydslPredicateExecutor<BaseCourse> {
    boolean existsByCode(String code);

    @Query("SELECT c FROM BaseCourse c JOIN c.departments d WHERE d.id = :departmentId")
    Page<BaseCourse> findByDepartmentId(@Param("departmentId") Long departmentId, Pageable pageable);
}
