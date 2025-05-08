package com.shadcn.courseservice.repository;

import com.shadcn.courseservice.entity.StudentReference;
import com.shadcn.courseservice.entity.TeacherCourseRole;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.querydsl.QuerydslPredicateExecutor;

import java.util.List;
import java.util.Set;

public interface TeacherCourseRoleRepository extends JpaRepository<TeacherCourseRole, Long>, QuerydslPredicateExecutor<StudentReference> {
  Set<TeacherCourseRole> findByTeacherReferenceIdAndCourseId(Long teacherId, Long courseId);
}