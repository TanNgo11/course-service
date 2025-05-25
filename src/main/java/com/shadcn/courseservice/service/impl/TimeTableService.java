package com.shadcn.courseservice.service.impl;

import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.temporal.WeekFields;
import java.util.*;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.shadcn.courseservice.dto.response.timetable.TimetableResponse;
import com.shadcn.courseservice.entity.*;
import com.shadcn.courseservice.enums.RoomType;
import com.shadcn.courseservice.enums.TeacherRole;
import com.shadcn.courseservice.exception.AppException;
import com.shadcn.courseservice.exception.ErrorCode;
import com.shadcn.courseservice.mapper.TimetableMapper;
import com.shadcn.courseservice.repository.*;
import com.shadcn.courseservice.service.ITimeTableService;

import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;

@Service
@RequiredArgsConstructor
@Slf4j
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class TimeTableService implements ITimeTableService {
    CourseRepository courseRepository;
    TimeSlotRepository timeSlotRepository;
    SemesterRepository semesterRepository;
    RoomRepository roomRepository;
    TimeTableRepository timetableRepository;
    ClassSessionRepository classSessionRepository;
    TeacherReferenceRepository teacherReferenceRepository;
    TimetableMapper timetableMapper;

    @Override
    @Transactional
    public void generateTimetablesForSemester(Long semesterId) {
        var semester = semesterRepository
                .findById(semesterId)
                .orElseThrow(() -> new RuntimeException("Semester not found: " + semesterId));
        List<Course> courses = courseRepository.findBySemesterId(semesterId);

        List<TimeSlot> semesterTimeSlots =
                timeSlotRepository.findAllByDateBetween(semester.getStartDate(), semester.getEndDate());

        for (Course course : courses) {
            generateTimetableForCourse(course, semesterTimeSlots);
        }
    }

    private void generateTimetableForCourse(Course course, List<TimeSlot> semesterTimeSlots) {
        int numTimetables = course.getNumsOfTimetable();

        List<TeacherReference> availableTeachers = course.getTeacherReferences().stream()
                .filter(teacher -> !teacher.getAvailableTimeSlots().isEmpty())
                .collect(Collectors.toList());

        if (availableTeachers.isEmpty()) {
            throw new RuntimeException("No available teachers for course: " + course.getId());
        }

        if (availableTeachers.size() > 2) {
            throw new RuntimeException("Course " + course.getId() + " has more than 2 assigned teachers");
        }

        List<Room> availableRooms = roomRepository.findAllByCapacityGreaterThanEqual(course.getMaxStudents()).stream()
                .filter(room -> !room.getAvailableTimeSlots().isEmpty())
                .collect(Collectors.toList());

        if (availableRooms.isEmpty()) {
            throw new RuntimeException("No available rooms for course: " + course.getId());
        }

        for (int i = 0; i < numTimetables; i++) {
            createSingleTimetable(course, new ArrayList<>(semesterTimeSlots), availableTeachers, availableRooms);
        }
    }

    private void createSingleTimetable(
            Course course, List<TimeSlot> semesterTimeSlots, List<TeacherReference> teachers, List<Room> rooms) {
        Timetable timetable = Timetable.builder()
                .course(course)
                .daysOfWeek(new HashSet<>())
                .classSessions(new ArrayList<>())
                .students(new ArrayList<>())
                .build();

        BaseCourse baseCourse = course.getBaseCourse();

        int theorySessionsPerWeek = course.getTheorySessionsPerWeek() != null
                ? course.getTheorySessionsPerWeek()
                : (int) Math.ceil((double) baseCourse.getTheorySessionsHours() / (2 * 10));

        int practiceSessionsPerWeek = course.getPracticeSessionsPerWeek() != null
                ? course.getPracticeSessionsPerWeek()
                : (int) Math.ceil((double) baseCourse.getPracticeSessionsHours() / (4 * 10));

        TeacherReference theoryTeacher = null;
        TeacherReference practiceTeacher = null;

        for (TeacherReference teacher : teachers) {
            Set<TeacherRole> roles = teacher.getTeacherCourseRoles().stream()
                    .filter(tcr -> tcr.getCourse().getId().equals(course.getId()))
                    .flatMap(tcr -> tcr.getRoles().stream())
                    .collect(Collectors.toSet());

            List<TimeSlot> availableSlots = semesterTimeSlots.stream()
                    .filter(slot -> teacher.getAvailableTimeSlots().contains(slot.getId()))
                    .toList();

            if (!availableSlots.isEmpty()) {
                if (roles.contains(TeacherRole.THEORY_TEACHER)) {
                    theoryTeacher = teacher;
                }
                if (roles.contains(TeacherRole.PRACTICE_TEACHER)) {
                    practiceTeacher = teacher;
                }
            }
        }

        if (baseCourse.getNumsOfPracticeSessions() == 0) {
            practiceTeacher = theoryTeacher;
        }

        if (theorySessionsPerWeek > 0 && theoryTeacher == null) {
            throw new RuntimeException("No available teacher for theory sessions in course: " + course.getId());
        }
        if (practiceSessionsPerWeek > 0 && practiceTeacher == null) {
            throw new RuntimeException("No available teacher for practice sessions in course: " + course.getId());
        }

        List<TimeSlot> selectedPracticeSlots = new ArrayList<>();
        List<TimeSlot> selectedTheorySlots = new ArrayList<>();

        int totalWeeks = 10;

        // Ưu tiên xếp LAB trước
        if (practiceSessionsPerWeek > 0 && practiceTeacher != null) {
            TeacherReference finalPracticeTeacher = practiceTeacher;
            List<TimeSlot> teacherAvailableSlots = semesterTimeSlots.stream()
                    .filter(slot -> finalPracticeTeacher.getAvailableTimeSlots().contains(slot.getId()))
                    .collect(Collectors.toList());
            selectedPracticeSlots =
                    selectTimeSlots(teacherAvailableSlots, practiceSessionsPerWeek, totalWeeks, rooms, 4, RoomType.LAB);
            // Loại bỏ các khe đã dùng cho LAB
            semesterTimeSlots.removeAll(selectedPracticeSlots);
        }

        // Xếp LECTURE sau
        if (theorySessionsPerWeek > 0 && theoryTeacher != null) {
            TeacherReference finalTheoryTeacher = theoryTeacher;
            List<TimeSlot> teacherAvailableSlots = semesterTimeSlots.stream()
                    .filter(slot -> finalTheoryTeacher.getAvailableTimeSlots().contains(slot.getId()))
                    .collect(Collectors.toList());
            selectedTheorySlots = selectTimeSlots(
                    teacherAvailableSlots, theorySessionsPerWeek, totalWeeks, rooms, 2, RoomType.LECTURE);
        }

        // Kết hợp slots
        List<TimeSlot> selectedSlots = new ArrayList<>();
        selectedSlots.addAll(selectedPracticeSlots);
        selectedSlots.addAll(selectedTheorySlots);

        // Tạo class sessions
        for (TimeSlot slot : selectedSlots) {
            int durationHours =
                    (slot.getEndTime().toSecondOfDay() - slot.getStartTime().toSecondOfDay()) / 3600;
            RoomType requiredRoomType = durationHours >= 4 ? RoomType.LAB : RoomType.LECTURE;
            TeacherReference assignedTeacher = durationHours >= 4 ? practiceTeacher : theoryTeacher;

            Room availableRoom = rooms.stream()
                    .filter(room -> room.getAvailableTimeSlots().contains(slot.getId()))
                    .filter(room -> room.getRoomType() == requiredRoomType)
                    .findFirst()
                    .orElseThrow(() -> new RuntimeException("No available room for slot: " + slot.getId()));

            ClassSession session = ClassSession.builder()
                    .timetable(timetable)
                    .timeSlot(slot)
                    .room(availableRoom)
                    .teacher(assignedTeacher)
                    .sessionDate(slot.getDate())
                    .build();

            timetable.getClassSessions().add(session);
            timetable.getDaysOfWeek().add(slot.getDayOfWeek());

            // Cập nhật available slots
            assignedTeacher.getAvailableTimeSlots().remove(slot.getId());
            availableRoom.getAvailableTimeSlots().remove(slot.getId());
        }

        // Lưu timetable và sessions
        timetableRepository.save(timetable);
        classSessionRepository.saveAll(timetable.getClassSessions());

        // Cập nhật teacher và room
        if (theoryTeacher != null) teacherReferenceRepository.save(theoryTeacher);
        if (practiceTeacher != null && practiceTeacher != theoryTeacher)
            teacherReferenceRepository.save(practiceTeacher);
        roomRepository.saveAll(rooms);
    }

    @Override
    public List<TimetableResponse> getTimetableByCourseId(Long courseId) {
        List<Timetable> timetables = timetableRepository.findByCourseId(courseId);
        if (timetables.isEmpty()) {
            log.warn("No timetables found for course ID: {}", courseId);
            return Collections.emptyList();
        }

        log.info("Found {} timetables for course ID: {}", timetables.size(), courseId);
        return timetableMapper.toTimetableResponseList(timetables);
    }

    @Override
    public List<TimetableResponse> getTimetableByTeacherId(Long teacherId, Long semesterId) {
        Semester semester = semesterRepository
                .findById(semesterId)
                .orElseThrow(() -> new AppException(ErrorCode.SEMESTER_NOT_FOUND));

        TeacherReference teacherReference = teacherReferenceRepository
                .findByTeacherId(teacherId)
                .orElseThrow(() -> new AppException(ErrorCode.TEACHER_NOT_FOUND));

        List<Course> courses = courseRepository.findByTeacherReferencesAndSemester(teacherReference, semester);
        List<Timetable> timetables = new ArrayList<>();
        for (Course course : courses) {
            List<Timetable> courseTimetables = timetableRepository.findByCourseId(course.getId());
            timetables.addAll(courseTimetables);
        }
        return timetableMapper.toTimetableResponseList(timetables);
    }

    private List<TimeSlot> selectTimeSlots(
            List<TimeSlot> availableSlots,
            int sessionsPerWeek,
            int weeks,
            List<Room> rooms,
            int sessionHours,
            RoomType requiredRoomType) {
        List<TimeSlot> selectedSlots = new ArrayList<>();
        log.info(
                "Selecting slots for {}: sessionsPerWeek={}, weeks={}, sessionHours={}",
                requiredRoomType,
                sessionsPerWeek,
                weeks,
                sessionHours);

        availableSlots.sort(Comparator.comparing(TimeSlot::getDate).thenComparing(TimeSlot::getStartTime));
        if (availableSlots.isEmpty()) {
            log.error("No available slots for {}", requiredRoomType);
            throw new RuntimeException("No available slots for " + requiredRoomType + " sessions");
        }

        LocalDate currentDate = availableSlots.get(0).getDate();
        int sessionsAdded = 0;
        int currentWeek = 0;

        if (sessionHours == 4) {
            log.info("Processing LAB slots (4 hours)");
            while (currentWeek < weeks && sessionsAdded < sessionsPerWeek * weeks) {
                LocalDate finalCurrentDate = currentDate;
                List<TimeSlot> weekSlots = availableSlots.stream()
                        .filter(slot -> isSameWeek(slot.getDate(), finalCurrentDate))
                        .filter(slot -> {
                            int durationHours = (slot.getEndTime().toSecondOfDay()
                                            - slot.getStartTime().toSecondOfDay())
                                    / 3600;
                            return durationHours == 2;
                        })
                        .sorted(Comparator.comparing(TimeSlot::getDate).thenComparing(TimeSlot::getStartTime))
                        .collect(Collectors.toList());
                log.info("Week starting {}: found {} slots", finalCurrentDate, weekSlots.size());
                log.info(
                        "Available slots: {}",
                        weekSlots.stream()
                                .map(slot -> String.format(
                                        "ID=%d, Date=%s, Time=%s-%s",
                                        slot.getId(), slot.getDate(), slot.getStartTime(), slot.getEndTime()))
                                .collect(Collectors.toList()));

                int sessionsThisWeek = 0;
                for (int i = 0; i < weekSlots.size() - 1 && sessionsThisWeek < sessionsPerWeek; i++) {
                    TimeSlot slot1 = weekSlots.get(i);
                    TimeSlot slot2 = weekSlots.get(i + 1);

                    // Nới lỏng điều kiện: chấp nhận gap ≤ 5 phút
                    long timeGapSeconds = slot2.getStartTime().toSecondOfDay()
                            - slot1.getEndTime().toSecondOfDay();
                    if (slot1.getDate().equals(slot2.getDate())
                            && slot1.getDayOfWeek().equals(slot2.getDayOfWeek())
                            && timeGapSeconds >= 0
                            && timeGapSeconds <= 300) { // Gap ≤ 5 phút
                        Optional<Room> availableRoom = rooms.stream()
                                .filter(room -> room.getAvailableTimeSlots().contains(slot1.getId())
                                        && room.getAvailableTimeSlots().contains(slot2.getId())
                                        && room.getRoomType() == requiredRoomType)
                                .findFirst();
                        if (availableRoom.isPresent()) {
                            selectedSlots.add(slot1);
                            selectedSlots.add(slot2);
                            sessionsAdded++;
                            sessionsThisWeek++;
                            i++; // Bỏ qua slot2
                            log.info(
                                    "Added LAB session: slots {} and {} (gap: {} seconds, room: {})",
                                    slot1.getId(),
                                    slot2.getId(),
                                    timeGapSeconds,
                                    availableRoom.get().getId());
                        } else {
                            log.warn("No LAB room available for slot pair {} and {}", slot1.getId(), slot2.getId());
                        }
                    } else {
                        log.warn(
                                "Slots {} ({}-{}) and {} ({}-{}) are not consecutive or have gap {} seconds",
                                slot1.getId(),
                                slot1.getStartTime(),
                                slot1.getEndTime(),
                                slot2.getId(),
                                slot2.getStartTime(),
                                slot2.getEndTime(),
                                timeGapSeconds);
                    }
                }
                currentDate = currentDate.plusDays(7);
                currentWeek++;
            }
        } else {
            // Logic cho LECTURE (giữ nguyên)
            log.info("Processing LECTURE slots (2 hours)");
            while (currentWeek < weeks && sessionsAdded < sessionsPerWeek * weeks) {
                LocalDate finalCurrentDate = currentDate;
                List<TimeSlot> weekSlots = availableSlots.stream()
                        .filter(slot -> isSameWeek(slot.getDate(), finalCurrentDate))
                        .filter(slot -> {
                            int durationHours = (slot.getEndTime().toSecondOfDay()
                                            - slot.getStartTime().toSecondOfDay())
                                    / 3600;
                            return durationHours == sessionHours;
                        })
                        .sorted(Comparator.comparing(TimeSlot::getStartTime))
                        .collect(Collectors.toList());
                log.info("Week starting {}: found {} slots", finalCurrentDate, weekSlots.size());

                int sessionsThisWeek = 0;
                for (TimeSlot slot : weekSlots) {
                    if (sessionsThisWeek >= sessionsPerWeek) break;

                    Optional<Room> availableRoom = rooms.stream()
                            .filter(room -> room.getAvailableTimeSlots().contains(slot.getId())
                                    && room.getRoomType() == requiredRoomType)
                            .findFirst();
                    log.info(
                            "Checking slot {} ({}-{}): roomAvailable={}",
                            slot.getId(),
                            slot.getStartTime(),
                            slot.getEndTime(),
                            availableRoom.isPresent());

                    if (availableRoom.isPresent()) {
                        selectedSlots.add(slot);
                        sessionsAdded++;
                        sessionsThisWeek++;
                        log.info("Added LECTURE session: slot {}", slot.getId());
                    }
                }
                currentDate = currentDate.plusDays(7);
                currentWeek++;
            }
        }

        int requiredSlots = sessionsPerWeek * weeks * (sessionHours == 4 ? 2 : 1);
        log.info("Selected {} slots for {} (required: {})", selectedSlots.size(), requiredRoomType, requiredSlots);
        if (selectedSlots.size() < requiredSlots) {
            log.error(
                    "Not enough slots selected for {}: got {}, needed {}",
                    requiredRoomType,
                    selectedSlots.size(),
                    requiredSlots);
            throw new RuntimeException("Not enough available slots for " + requiredRoomType + " sessions");
        }

        return selectedSlots;
    }

    private boolean isSameWeek(LocalDate date1, LocalDate date2) {
        WeekFields weekFields = WeekFields.of(DayOfWeek.MONDAY, 1);
        return date1.get(weekFields.weekOfWeekBasedYear()) == date2.get(weekFields.weekOfWeekBasedYear())
                && date1.getYear() == date2.getYear();
    }
}
