package com.shadcn.courseservice.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.querydsl.QuerydslPredicateExecutor;

import com.shadcn.courseservice.entity.Lesson;

public interface LessonRepository extends JpaRepository<Lesson, Long>, QuerydslPredicateExecutor<Lesson> {}
