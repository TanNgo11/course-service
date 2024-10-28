package com.shadcn.courseservice.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.querydsl.QuerydslPredicateExecutor;

import com.shadcn.courseservice.entity.StudentReference;

public interface StudentReferenceRepository
        extends JpaRepository<StudentReference, Long>, QuerydslPredicateExecutor<StudentReference> {}
