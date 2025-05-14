package com.shadcn.courseservice.service.impl;

import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.shadcn.courseservice.entity.TimeSlot;
import com.shadcn.courseservice.repository.SemesterRepository;
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
    SemesterRepository semesterRepository;

    @Override
    @Transactional
    public void initializeTimeSlots(LocalDate startDate, LocalDate endDate) {
        LocalTime[][] timeRanges = {
            {LocalTime.of(7, 30), LocalTime.of(9, 30)},
            {LocalTime.of(9, 30), LocalTime.of(11, 30)},
            {LocalTime.of(12, 30), LocalTime.of(14, 30)},
            {LocalTime.of(14, 30), LocalTime.of(16, 30)},
            {LocalTime.of(16, 30), LocalTime.of(18, 30)},
            {LocalTime.of(18, 30), LocalTime.of(20, 30)}
        };
        List<TimeSlot> timeSlots = new ArrayList<>();
        for (LocalDate date = startDate; !date.isAfter(endDate); date = date.plusDays(1)) {
            DayOfWeek day = date.getDayOfWeek();
            for (LocalTime[] range : timeRanges) {
                TimeSlot timeSlot = TimeSlot.builder()
                        .date(date)
                        .dayOfWeek(day)
                        .startTime(range[0])
                        .endTime(range[1])
                        .build();
                timeSlots.add(timeSlot);
            }
        }
        timeSlotRepository.saveAll(timeSlots);
    }

    @Override
    @Transactional
    public void initializeTimeSlotsForSemester(Long semesterId) {
        var semester = semesterRepository
                .findById(semesterId)
                .orElseThrow(() -> new IllegalArgumentException("Semester not found"));

        LocalDate startDate = semester.getStartDate();
        LocalDate endDate = semester.getEndDate();

        boolean anyTimeSlotExists = timeSlotRepository.existsByDateBetween(startDate, endDate);
        if (anyTimeSlotExists) {
            log.info(
                    "Time slots already exist for semester {} between {} and {}. Skipping initialization.",
                    semesterId,
                    startDate,
                    endDate);
            return;
        }

        this.initializeTimeSlots(startDate, endDate);
    }
}
