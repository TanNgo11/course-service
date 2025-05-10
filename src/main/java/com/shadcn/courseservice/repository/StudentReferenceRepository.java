package com.shadcn.courseservice.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.querydsl.QuerydslPredicateExecutor;
import org.springframework.stereotype.Repository;

import com.shadcn.courseservice.entity.Course;
import com.shadcn.courseservice.entity.StudentReference;

@Repository
public interface StudentReferenceRepository
        extends JpaRepository<StudentReference, Long>, QuerydslPredicateExecutor<StudentReference> {

    Optional<StudentReference> findByStudentId(Long studentId);

    List<StudentReference> findAllByCourses(List<Course> courses);
}
