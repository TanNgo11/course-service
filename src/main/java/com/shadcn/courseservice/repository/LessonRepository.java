package com.shadcn.courseservice.repository;

import com.shadcn.courseservice.entity.Course;
import com.shadcn.courseservice.entity.Lesson;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.querydsl.QuerydslPredicateExecutor;

public interface LessonRepository extends JpaRepository<Lesson, Long>, QuerydslPredicateExecutor<Lesson> {}
