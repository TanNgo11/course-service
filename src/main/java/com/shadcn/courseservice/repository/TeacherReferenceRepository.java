package com.shadcn.courseservice.repository;

import com.shadcn.courseservice.entity.Lesson;
import com.shadcn.courseservice.entity.TeacherReference;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.querydsl.QuerydslPredicateExecutor;

public interface TeacherReferenceRepository extends JpaRepository<TeacherReference, Long>, QuerydslPredicateExecutor<Lesson> {}
