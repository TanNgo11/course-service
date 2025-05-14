package com.shadcn.courseservice.service;

import java.time.LocalDate;

public interface ITimeSlotService {
    void initializeTimeSlots(LocalDate startDate, LocalDate endDate);

    void initializeTimeSlotsForSemester(Long semesterId);
}
