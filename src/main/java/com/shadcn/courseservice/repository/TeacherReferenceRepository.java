package com.shadcn.courseservice.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.querydsl.QuerydslPredicateExecutor;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.shadcn.courseservice.entity.Course;
import com.shadcn.courseservice.entity.TeacherReference;

@Repository
public interface TeacherReferenceRepository
        extends JpaRepository<TeacherReference, Long>, QuerydslPredicateExecutor<TeacherReference> {

    Optional<TeacherReference> findByTeacherId(Long teacherId);

    Optional<TeacherReference> findByUsername(String username);

    List<TeacherReference> findAllByCourses(List<Course> courses);

    @Query("SELECT DISTINCT t FROM TeacherReference t " +
            "JOIN t.courses c " +
            "WHERE c.id IN :courseIds")
    List<TeacherReference> findAllByCourseIds(@Param("courseIds") List<Long> courseIds);
}
