package com.shadcn.courseservice.service;

import com.shadcn.courseservice.dto.response.timeslot.TimeslotResponse;

import java.time.LocalDate;
import java.util.List;

public interface ITimeSlotService {
    void initializeTimeSlots(LocalDate startDate, LocalDate endDate);

    void initializeTimeSlotsForSemester(Long semesterId);
    
    void initializeTimeSlotsForAllTeachersBySemesterId(LocalDate startDate, LocalDate endDate);
    
    void removeTimeslotByTeacherIdAndTimeSlotId(Long teacherId, Long timeSlotId);
    
    void initializeTimeSlotsForAllRoomsBySemesterId(LocalDate startDate, LocalDate endDate);

    List<TimeslotResponse> getTimeSlotsByTeacherIdAndSemesterId(Long teacherId, Long semesterId);
    
}
