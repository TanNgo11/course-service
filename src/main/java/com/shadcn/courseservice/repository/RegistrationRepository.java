package com.shadcn.courseservice.repository;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.shadcn.courseservice.entity.Registration;

public interface RegistrationRepository extends JpaRepository<Registration, Long> {
    @Query("SELECT r FROM Registration r WHERE r.studentReference.studentId = :studentId AND r.course.id = :courseId")
    Registration findByStudentIdAndCourseId(@Param("studentId") long studentId, @Param("courseId") long courseId);

    @Query("DELETE FROM Registration r WHERE r.course.id = :courseId")
    void deleteAllByCourseId(@Param("courseId") long courseId);

    @Query("DELETE FROM Registration r WHERE r.course.id = :courseId")
    void deleteAllByStudentId(@Param("courseId") long studentId);

    @Query("SELECT r FROM Registration r WHERE r.studentReference.studentId = :studentId")
    Page<Registration> findAllByStudentProfileId(@Param("studentId") long studentId, Pageable pageable);

    @Query("SELECT r FROM Registration r WHERE r.course.id = :courseId")
    Page<Registration> findAllByCourseId(@Param("courseId") long courseId, Pageable pageable);

    @Query("SELECT r FROM Registration r WHERE r.semester.id = :semesterId")
    Page<Registration> findAllBySemesterId(@Param("semesterId") long semesterId, Pageable pageable);

    @Query("SELECT r FROM Registration r WHERE r.semester.id = :semesterId")
    List<Registration> findAllRegistrationBySemesterId(@Param("semesterId") long semesterId);

    @Query(
            "SELECT CASE WHEN EXISTS (SELECT r FROM Registration r WHERE r.studentReference.studentId = :studentId AND r.course.id = :courseId AND r.semester.id = :semesterId) THEN true ELSE false END")
    boolean existsByStudentIdAndCourseIdAndSemesterId(
            @Param("studentId") long studentId, @Param("courseId") long courseId, @Param("semesterId") long semesterId);

    @Query(
            "SELECT r FROM Registration r WHERE r.studentReference.studentId = :studentId AND r.semester.id = :semesterId  AND r.status = 'APPROVED'")
    Page<Registration> getRegistrationByStudentIdAndSemesterId(long studentId, long semesterId, Pageable pageable);

    @Query(
            "SELECT r FROM Registration r WHERE r.studentReference.studentId = :studentId AND r.semester.id = :semesterId AND r.course.baseCourse.code = :courseCode")
    Registration findByRegistrationByStudentIdAndCourseCodeAndSemesterId(
            long studentId, String courseCode, long semesterId);
    
}
