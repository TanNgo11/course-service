package com.shadcn.courseservice.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.querydsl.QuerydslPredicateExecutor;
import org.springframework.data.repository.query.Param;

import com.shadcn.courseservice.entity.TeacherProfile;

public interface TeacherProfileRepository
        extends JpaRepository<TeacherProfile, Long>, QuerydslPredicateExecutor<TeacherProfile> {

    @Query("SELECT s FROM TeacherProfile s WHERE s.teacherId = :teacherId")
    TeacherProfile getTeacherProfileByTeacherId(@Param("teacherId") long teacherId);
}
