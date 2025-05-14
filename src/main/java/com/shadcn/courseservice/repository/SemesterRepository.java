package com.shadcn.courseservice.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.querydsl.QuerydslPredicateExecutor;

import com.shadcn.courseservice.entity.AcademicYear;
import com.shadcn.courseservice.entity.Semester;

import feign.Param;

public interface SemesterRepository extends JpaRepository<Semester, Long>, QuerydslPredicateExecutor<Semester> {
    @Query(
            "SELECT CASE WHEN COUNT(c) > 0 THEN TRUE ELSE FALSE END FROM Semester s JOIN s.courses c WHERE s.id = :semesterId AND c.id = :courseId")
    boolean existsCourseInSemester(@Param("semesterId") Long semesterId, @Param("courseId") Long courseId);

    Semester findByRegistrationOpen(boolean registrationOpen);

    @Query("SELECT s FROM Semester s WHERE s.semesterActive = :semesterActive")
    Semester findBySemesterActive(@Param("semesterActive") boolean semesterActive);

    Page<Semester> findAllByAcademicYear(AcademicYear academicYear, Pageable pageable);
}
