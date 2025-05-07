package com.shadcn.courseservice.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.querydsl.QuerydslPredicateExecutor;

import com.shadcn.courseservice.entity.Attendance;
import com.shadcn.courseservice.entity.ClassSession;
import com.shadcn.courseservice.entity.StudentReference;

public interface AttendanceRepository extends JpaRepository<Attendance, Long>, QuerydslPredicateExecutor<Attendance> {
    Optional<Attendance> findByClassSessionAndStudent(ClassSession classSession, StudentReference student);

    List<Attendance> findByClassSession(ClassSession classSession);

    List<Attendance> findByStudent(StudentReference student);
}
