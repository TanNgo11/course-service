package com.shadcn.courseservice.service;

import org.bouncycastle.asn1.bc.BCObjectIdentifiers;

import java.time.LocalDate;

public interface ITimeSlotService {
    void initializeTimeSlots(LocalDate startDate, LocalDate endDate);

    void initializeTimeSlotsForSemester(Long semesterId);
    
    void initializeTimeSlotsForAllTeachersBySemesterId(LocalDate startDate, LocalDate endDate);
    
    void removeTimeslotByTeacherIdAndTimeSlotId(Long teacherId, Long timeSlotId);
    
    void initializeTimeSlotsForAllRoomsBySemesterId(LocalDate startDate, LocalDate endDate);
    
}
