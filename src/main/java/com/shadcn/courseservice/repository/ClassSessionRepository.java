package com.shadcn.courseservice.repository;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.querydsl.QuerydslPredicateExecutor;

import com.shadcn.courseservice.entity.ClassSession;
import com.shadcn.courseservice.entity.Room;
import com.shadcn.courseservice.entity.TeacherReference;
import com.shadcn.courseservice.entity.TimeSlot;
import com.shadcn.courseservice.entity.Timetable;

public interface ClassSessionRepository
        extends JpaRepository<ClassSession, Long>, QuerydslPredicateExecutor<ClassSession> {

    Optional<ClassSession> findById(Long id);

    List<ClassSession> findByTimetable(Timetable timetable);

    List<ClassSession> findByRoomAndSessionDate(Room room, LocalDate sessionDate);

    List<ClassSession> findByTeacherAndSessionDate(TeacherReference teacher, LocalDate sessionDate);

    List<ClassSession> findByTimeSlotAndSessionDate(TimeSlot timeSlot, LocalDate sessionDate);
}
