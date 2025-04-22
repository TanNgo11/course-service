package com.shadcn.courseservice.service.impl;

import java.time.DayOfWeek;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.shadcn.courseservice.entity.TimeSlot;
import com.shadcn.courseservice.repository.TimeSlotRepository;
import com.shadcn.courseservice.service.ITimeSlotService;

import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;

@Service
@RequiredArgsConstructor
@Slf4j
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class TimeSlotService implements ITimeSlotService {
    TimeSlotRepository timeSlotRepository;

    @Override
    @Transactional
    public void initializeTimeSlots() {
        LocalTime[][] timeRanges = {
            {LocalTime.of(7, 30), LocalTime.of(9, 30)},
            {LocalTime.of(9, 30), LocalTime.of(11, 30)},
            {LocalTime.of(12, 30), LocalTime.of(14, 30)},
            {LocalTime.of(14, 30), LocalTime.of(16, 30)},
            {LocalTime.of(16, 30), LocalTime.of(18, 30)},
            {LocalTime.of(18, 30), LocalTime.of(20, 30)}
        };
        List<TimeSlot> timeSlots = new ArrayList<>();
        for (DayOfWeek day : DayOfWeek.values()) {
            for (LocalTime[] range : timeRanges) {
                TimeSlot timeSlot = TimeSlot.builder()
                        .dayOfWeek(day)
                        .startTime(range[0])
                        .endTime(range[1])
                        .build();
            }
        }
        timeSlotRepository.saveAll(timeSlots);
    }
}
