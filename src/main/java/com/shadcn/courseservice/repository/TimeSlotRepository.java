package com.shadcn.courseservice.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.querydsl.QuerydslPredicateExecutor;

import com.shadcn.courseservice.entity.TimeSlot;

public interface TimeSlotRepository extends JpaRepository<TimeSlot, Long>, QuerydslPredicateExecutor<TimeSlot> {}
