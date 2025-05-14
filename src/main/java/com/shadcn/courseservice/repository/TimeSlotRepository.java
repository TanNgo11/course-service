package com.shadcn.courseservice.repository;

import java.time.LocalDate;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.querydsl.QuerydslPredicateExecutor;
import org.springframework.stereotype.Repository;

import com.shadcn.courseservice.entity.TimeSlot;

@Repository
public interface TimeSlotRepository extends JpaRepository<TimeSlot, Long>, QuerydslPredicateExecutor<TimeSlot> {
    TimeSlot getTimeSlotsById(Long timeSlotId);

    boolean existsByDateBetween(LocalDate startDate, LocalDate endDate);
}
