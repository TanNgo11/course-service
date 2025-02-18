package com.shadcn.courseservice.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.querydsl.QuerydslPredicateExecutor;

import com.shadcn.courseservice.entity.Semester;

import feign.Param;

public interface SemesterRepository extends JpaRepository<Semester, Long>, QuerydslPredicateExecutor<Semester> {
    @Query(
            "SELECT CASE WHEN COUNT(c) > 0 THEN TRUE ELSE FALSE END FROM Semester s JOIN s.courses c WHERE s.id = :semesterId AND c.id = :courseId")
    boolean existsCourseInSemester(@Param("semesterId") Long semesterId, @Param("courseId") Long courseId);

    Semester findByRegistrationOpen(boolean registrationOpen);
}
