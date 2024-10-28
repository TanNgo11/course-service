package com.shadcn.courseservice.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.querydsl.QuerydslPredicateExecutor;

import com.shadcn.courseservice.entity.TeacherReference;

public interface TeacherReferenceRepository
        extends JpaRepository<TeacherReference, Long>, QuerydslPredicateExecutor<TeacherReference> {}
