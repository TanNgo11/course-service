package com.shadcn.courseservice.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.querydsl.QuerydslPredicateExecutor;

import com.shadcn.courseservice.entity.BaseCourse;

public interface BaseCourseRepository extends JpaRepository<BaseCourse, Long>, QuerydslPredicateExecutor<BaseCourse> {
    boolean existsByCode(String code);
}
