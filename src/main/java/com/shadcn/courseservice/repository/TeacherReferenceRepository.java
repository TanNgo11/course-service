package com.shadcn.courseservice.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.querydsl.QuerydslPredicateExecutor;

import com.shadcn.courseservice.entity.Course;
import com.shadcn.courseservice.entity.TeacherReference;

public interface TeacherReferenceRepository
        extends JpaRepository<TeacherReference, Long>, QuerydslPredicateExecutor<TeacherReference> {

    Optional<TeacherReference> findByTeacherId(Long teacherId);

    Optional<TeacherReference> findByUsername(String username);

    List<TeacherReference> findAllByCourses(List<Course> courses);
}
