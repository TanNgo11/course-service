package com.shadcn.courseservice.repository;

import java.util.Set;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.querydsl.QuerydslPredicateExecutor;

import com.shadcn.courseservice.entity.StudentReference;
import com.shadcn.courseservice.entity.TeacherCourseRole;

public interface TeacherCourseRoleRepository
        extends JpaRepository<TeacherCourseRole, Long>, QuerydslPredicateExecutor<StudentReference> {
    Set<TeacherCourseRole> findByTeacherReferenceIdAndCourseId(Long teacherId, Long courseId);
}
