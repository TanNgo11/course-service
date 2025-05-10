package com.shadcn.courseservice.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.querydsl.QuerydslPredicateExecutor;

import com.shadcn.courseservice.entity.Timetable;
import org.springframework.stereotype.Repository;

@Repository
public interface TimeTableRepository extends JpaRepository<Timetable, Long>, QuerydslPredicateExecutor<Timetable> {

    Timetable getTimetableById(Long timetableId);
}
