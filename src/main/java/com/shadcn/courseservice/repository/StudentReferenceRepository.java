package com.shadcn.courseservice.repository;

import com.shadcn.courseservice.entity.Course;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.querydsl.QuerydslPredicateExecutor;

import com.shadcn.courseservice.entity.StudentReference;

import java.util.List;
import java.util.Optional;

public interface StudentReferenceRepository
        extends JpaRepository<StudentReference, Long>, QuerydslPredicateExecutor<StudentReference> {
    
    Optional<StudentReference> findByStudentId(Long studentId);
    
    List<StudentReference> findAllByCourses(List<Course> courses);
}
