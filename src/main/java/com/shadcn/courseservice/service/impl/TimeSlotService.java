package com.shadcn.courseservice.service.impl;

import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.shadcn.courseservice.dto.response.timeslot.TimeslotResponse;
import com.shadcn.courseservice.entity.Room;
import com.shadcn.courseservice.entity.TeacherReference;
import com.shadcn.courseservice.entity.TimeSlot;
import com.shadcn.courseservice.enums.RoomStatus;
import com.shadcn.courseservice.mapper.TimeslotMapper;
import com.shadcn.courseservice.repository.RoomRepository;
import com.shadcn.courseservice.repository.SemesterRepository;
import com.shadcn.courseservice.repository.TeacherReferenceRepository;
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
    TeacherReferenceRepository teacherReferenceRepository;
    RoomRepository roomRepository;
    TimeslotMapper timeslotMapper;
    JdbcTemplate jdbcTemplate;

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
            //            return;
        }

        //        this.initializeTimeSlots(startDate, endDate);
        this.initializeTimeSlotsForAllTeachersBySemesterId(startDate, endDate);
        this.initializeTimeSlotsForAllRoomsBySemesterId(startDate, endDate);
    }

    @Override
    @Transactional
    public void initializeTimeSlotsForAllRoomsBySemesterId(LocalDate startDate, LocalDate endDate) {
        List<TimeSlot> timeSlots = timeSlotRepository.findAllByDateBetween(startDate, endDate);
        List<Long> timeSlotIds = timeSlots.stream().map(TimeSlot::getId).collect(Collectors.toList());
        log.info("Found {} time slots between {} and {}", timeSlots.size(), startDate, endDate);

        if (timeSlots.isEmpty()) {
            log.warn("No time slots found for the given semester period");
            return;
        }

        List<Room> rooms = roomRepository.findAllByStatus(RoomStatus.AVAILABLE);
        log.info("Found {} available rooms", rooms.size());

        log.info("Deleting existing records in room_available_slots for date range {} to {}", startDate, endDate);
        jdbcTemplate.update(
                "DELETE FROM room_available_slots WHERE time_slot_id IN (SELECT id FROM time_slot WHERE date BETWEEN ? AND ?)",
                startDate,
                endDate);

        String sql = "INSERT IGNORE INTO room_available_slots (room_id, time_slot_id) VALUES (?, ?)";
        int batchSize = 500; // Giảm batchSize để tránh vượt giới hạn MySQL
        List<Object[]> batchArgs = new ArrayList<>();
        int totalRecords = 0;

        for (Room room : rooms) {
            log.info("Processing room {} with {} time slots", room.getId(), timeSlotIds.size());
            for (Long timeSlotId : timeSlotIds) {
                batchArgs.add(new Object[] {room.getId(), timeSlotId});
                if (batchArgs.size() >= batchSize) {
                    int[] results = jdbcTemplate.batchUpdate(sql, batchArgs);
                    totalRecords += results.length;
                    log.info("Inserted {} records for room_available_slots (batch)", results.length);
                    batchArgs.clear();
                }
            }
        }
        if (!batchArgs.isEmpty()) {
            int[] results = jdbcTemplate.batchUpdate(sql, batchArgs);
            totalRecords += results.length;
            log.info("Inserted {} records for room_available_slots (final batch)", results.length);
        }
        log.info("Total records inserted for room_available_slots: {}", totalRecords);
    }

    @Override
    public List<TimeslotResponse> getTimeSlotsByTeacherIdAndSemesterId(Long teacherId, Long semesterId) {
        var semester = semesterRepository
                .findById(semesterId)
                .orElseThrow(() -> new IllegalArgumentException("Semester not found"));

        LocalDate startDate = semester.getStartDate();
        LocalDate endDate = semester.getEndDate();
        List<TimeslotResponse> timeSlots =
                timeSlotRepository.findAllByTeacherIdAndDateBetween(teacherId, startDate, endDate);

        return timeSlots;
    }

    @Override
    @Transactional
    public void initializeTimeSlotsForAllTeachersBySemesterId(LocalDate startDate, LocalDate endDate) {
        List<TimeSlot> timeSlots = timeSlotRepository.findAllByDateBetween(startDate, endDate);
        List<Long> timeSlotIds = timeSlots.stream().map(TimeSlot::getId).collect(Collectors.toList());
        log.info("Found {} time slots between {} and {}", timeSlots.size(), startDate, endDate);

        if (timeSlots.isEmpty()) {
            log.warn("No time slots found for the given semester period");
            return;
        }

        List<TeacherReference> teachers = teacherReferenceRepository.findAll();
        log.info("Found {} teachers", teachers.size());

        log.info("Deleting existing records in teacher_available_slots for date range {} to {}", startDate, endDate);
        jdbcTemplate.update(
                "DELETE FROM teacher_available_slots WHERE time_slot_id IN (SELECT id FROM time_slot WHERE date BETWEEN ? AND ?)",
                startDate,
                endDate);

        String sql = "INSERT IGNORE INTO teacher_available_slots (teacher_reference_id, time_slot_id) VALUES (?, ?)";
        int batchSize = 500; // Giảm batchSize để tránh vượt giới hạn MySQL
        List<Object[]> batchArgs = new ArrayList<>();
        int totalRecords = 0;

        for (TeacherReference teacher : teachers) {
            log.info("Processing teacher {} with {} time slots", teacher.getTeacherId(), timeSlotIds.size());
            for (Long timeSlotId : timeSlotIds) {
                batchArgs.add(new Object[] {teacher.getId(), timeSlotId});
                if (batchArgs.size() >= batchSize) {
                    int[] results = jdbcTemplate.batchUpdate(sql, batchArgs);
                    totalRecords += results.length;
                    log.info("Inserted {} records for teacher_available_slots (batch)", results.length);
                    batchArgs.clear();
                }
            }
        }
        if (!batchArgs.isEmpty()) {
            int[] results = jdbcTemplate.batchUpdate(sql, batchArgs);
            totalRecords += results.length;
            log.info("Inserted {} records for teacher_available_slots (final batch)", results.length);
        }
        log.info("Total records inserted for teacher_available_slots: {}", totalRecords);
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
