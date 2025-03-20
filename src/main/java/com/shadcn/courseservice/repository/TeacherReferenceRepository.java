package com.shadcn.courseservice.repository;

import com.shadcn.courseservice.entity.Course;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.querydsl.QuerydslPredicateExecutor;

import com.shadcn.courseservice.entity.TeacherReference;

import java.util.List;
import java.util.Optional;

public interface TeacherReferenceRepository
        extends JpaRepository<TeacherReference, Long>, QuerydslPredicateExecutor<TeacherReference> {
    
    Optional<TeacherReference> findByTeacherId(Long teacherId);

    List<TeacherReference> findAllByCourses(List<Course> courses);
 
}
