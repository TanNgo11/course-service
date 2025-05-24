package com.shadcn.courseservice.repository;

import com.shadcn.courseservice.entity.Timetable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.querydsl.QuerydslPredicateExecutor;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface TimeTableRepository extends JpaRepository<Timetable, Long>, QuerydslPredicateExecutor<Timetable> {

    Timetable getTimetableById(Long timetableId);

    List<Timetable> findByCourseId(Long courseId);


    
    
}
