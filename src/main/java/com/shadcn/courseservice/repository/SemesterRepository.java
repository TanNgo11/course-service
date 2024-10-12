package com.shadcn.courseservice.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.querydsl.QuerydslPredicateExecutor;

import com.shadcn.courseservice.entity.Semester;

public interface SemesterRepository extends JpaRepository<Semester, Long>, QuerydslPredicateExecutor<Semester> {
}
