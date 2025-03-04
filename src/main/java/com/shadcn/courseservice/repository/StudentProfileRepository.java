package com.shadcn.courseservice.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.querydsl.QuerydslPredicateExecutor;
import org.springframework.data.repository.query.Param;

import com.shadcn.courseservice.entity.StudentProfile;

public interface StudentProfileRepository
        extends JpaRepository<StudentProfile, Long>, QuerydslPredicateExecutor<StudentProfile> {

    @Query("SELECT s FROM StudentProfile s WHERE s.studentId = :studentId")
    StudentProfile getStudentProfileByStudentId(@Param("studentId") long studentId);
}
