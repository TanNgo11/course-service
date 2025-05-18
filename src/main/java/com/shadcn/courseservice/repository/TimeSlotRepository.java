package com.shadcn.courseservice.repository;

import java.time.LocalDate;
import java.util.List;
import java.util.Set;

import com.shadcn.courseservice.dto.response.timeslot.TimeslotResponse;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.querydsl.QuerydslPredicateExecutor;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.shadcn.courseservice.entity.TimeSlot;

@Repository
public interface TimeSlotRepository extends JpaRepository<TimeSlot, Long>, QuerydslPredicateExecutor<TimeSlot> {
    TimeSlot getTimeSlotsById(Long timeSlotId);

    boolean existsByDateBetween(LocalDate startDate, LocalDate endDate);

    List<TimeSlot> findAllByDateBetween(LocalDate startDate, LocalDate endDate);

    @Query("SELECT new com.shadcn.courseservice.dto.response.timeslot.TimeslotResponse(" +
            "ts.date, ts.dayOfWeek, ts.startTime, ts.endTime) " +
            "FROM TimeSlot ts " +
            "WHERE ts.id IN (" +
            "   SELECT tas FROM TeacherReference tr " +
            "   JOIN tr.availableTimeSlots tas " +
            "   WHERE tr.teacherId = :teacherId" +
            ") " +
            "AND ts.date BETWEEN :startDate AND :endDate")
    List<TimeslotResponse> findAllByTeacherIdAndDateBetween(
            @Param("teacherId") Long teacherId,
            @Param("startDate") LocalDate startDate,
            @Param("endDate") LocalDate endDate);

}
