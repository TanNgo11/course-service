package com.shadcn.courseservice.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.querydsl.QuerydslPredicateExecutor;
import org.springframework.stereotype.Repository;

import com.shadcn.courseservice.entity.Timetable;

@Repository
public interface TimeTableRepository extends JpaRepository<Timetable, Long>, QuerydslPredicateExecutor<Timetable> {

    Timetable getTimetableById(Long timetableId);

    List<Timetable> findByCourseId(Long courseId);
}
