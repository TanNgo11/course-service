package com.shadcn.courseservice.service.impl;

import com.shadcn.courseservice.entity.Room;
import com.shadcn.courseservice.entity.TeacherReference;
import com.shadcn.courseservice.entity.TimeSlot;
import com.shadcn.courseservice.enums.RoomStatus;
import com.shadcn.courseservice.repository.RoomRepository;
import com.shadcn.courseservice.repository.SemesterRepository;
import com.shadcn.courseservice.repository.TeacherReferenceRepository;
import com.shadcn.courseservice.repository.TimeSlotRepository;
import com.shadcn.courseservice.service.ITimeSlotService;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Slf4j
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class TimeSlotService implements ITimeSlotService {
    TimeSlotRepository timeSlotRepository;
    SemesterRepository semesterRepository;
    TeacherReferenceRepository teacherReferenceRepository;
    RoomRepository roomRepository;

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
        this.initializeTimeSlotsForAllTeachersBySemesterId(startDate, endDate);
        this.initializeTimeSlotsForAllRoomsBySemesterId(startDate, endDate);
    }

    @Override
    @Transactional
    public void initializeTimeSlotsForAllRoomsBySemesterId(LocalDate startDate, LocalDate endDate) {
        List<TimeSlot> timeSlots = timeSlotRepository.findAllByDateBetween(
                startDate, endDate);
        Set<Long> timeSlotIds = timeSlots.stream()
                .map(TimeSlot::getId)
                .collect(Collectors.toSet());
        if (timeSlots.isEmpty()) {
            return;
        }
        List<Room> rooms = roomRepository.findAllByStatus(RoomStatus.AVAILABLE);
        for (Room room : rooms) {
            room.setAvailableTimeSlots(timeSlotIds);
        }
        roomRepository.saveAll(rooms);
    }

    @Override
    @Transactional
    public void initializeTimeSlotsForAllTeachersBySemesterId(LocalDate startDate, LocalDate endDate) {
        List<TeacherReference> teachers = teacherReferenceRepository.findAll();
        List<TimeSlot> timeSlots = timeSlotRepository.findAllByDateBetween(
                startDate, endDate);
        Set<Long> timeSlotIds = timeSlots.stream()
                .map(TimeSlot::getId)
                .collect(Collectors.toSet());
        if (timeSlots.isEmpty()) {
            return;
        }
        for (TeacherReference teacher : teachers) {
            teacher.setAvailableTimeSlots(timeSlotIds);
            teacherReferenceRepository.save(teacher);
        }
    }

    @Override
    @Transactional
    public void removeTimeslotByTeacherIdAndTimeSlotId(Long teacherId, Long timeSlotId) {
        TeacherReference teacher = teacherReferenceRepository
                .findByTeacherId(teacherId)
                .orElseThrow(() -> new IllegalArgumentException("Teacher not found"));

        Set<Long> availableTimeSlots = teacher.getAvailableTimeSlots();
        if (availableTimeSlots.contains(timeSlotId)) {
            availableTimeSlots.remove(timeSlotId);
            teacherReferenceRepository.save(teacher);
        } else {
            log.warn("Time slot {} is not available for teacher {}", timeSlotId, teacherId);
        }
    }
}
