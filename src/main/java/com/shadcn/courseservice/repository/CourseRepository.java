package com.shadcn.courseservice.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.querydsl.QuerydslPredicateExecutor;

import com.shadcn.courseservice.entity.Course;

public interface CourseRepository extends JpaRepository<Course, Long>, QuerydslPredicateExecutor<Course> {}
