package com.shadcn.courseservice.repository;

import com.shadcn.courseservice.entity.TeacherReference;
import com.shadcn.courseservice.entity.TimeSlot;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.querydsl.QuerydslPredicateExecutor;

public interface TimeSlotRepository    extends JpaRepository<TimeSlot, Long>, QuerydslPredicateExecutor<TimeSlot> {
}
