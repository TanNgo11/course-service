package com.shadcn.courseservice.service.impl;

import java.time.DayOfWeek;
import java.time.LocalDate;
import java.util.List;
import java.util.Set;

import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import com.shadcn.courseservice.dto.request.attendance.class_session.ClassSessionCreationRequest;
import com.shadcn.courseservice.entity.*;
import com.shadcn.courseservice.enums.ClassSessionStatus;
import com.shadcn.courseservice.enums.ClassSessionType;
import com.shadcn.courseservice.repository.TimeSlotRepository;
import com.shadcn.courseservice.repository.TimeTableRepository;
import com.shadcn.courseservice.service.IClassSessionService;
import com.shadcn.courseservice.service.IScheduleService;

@Service
@RequiredArgsConstructor
@Slf4j
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class ScheduleService implements IScheduleService {
    IClassSessionService classSessionService;
    TimeTableRepository timeTableRepository;
    TimeSlotRepository timeSlotRepository;

    @Override
    public void generateTimeTable(Course course) {
        Timetable timetable = Timetable.builder()
                .course(course)
                .daysOfWeek(Set.of(DayOfWeek.MONDAY, DayOfWeek.WEDNESDAY))
                .students(course.getStudentReferences())

                .build();

        // Save the timetable to the database
        timeTableRepository.save(timetable);

        // Generate class sessions for the course
        generateTimeTableForCourse(course, timetable);
    }

    private void generateTimeTableForCourse(Course course, Timetable timetable) {
        int totalTheory = course.getNumsOfTheorySessions();
        int totalPractice = course.getNumsOfPracticeSessions();
        int perWeek = course.getSessionsPerWeek();
        Set<DayOfWeek> days = timetable.getDaysOfWeek();

        LocalDate currentDate = course.getStartDate();
        List<TimeSlot> allTimeSlots = timeSlotRepository.findAll(); // existing in the database

        int weekCount = 0;
        while (totalTheory > 0 || totalPractice > 0) {
            weekCount++;
            int sessionsCreatedThisWeek = 0;
            int theorySessionsThisWeek = 0;
            for (DayOfWeek day : days) {

                if (sessionsCreatedThisWeek >= perWeek) break;

                for (TimeSlot slot : allTimeSlots) {
                    if (slot.getDayOfWeek() != day) continue;

                    var classSessionType =
                            theorySessionsThisWeek >= 2 ? ClassSessionType.LAB : ClassSessionType.LECTURE;
//                    var teacherId = classSessionType == ClassSessionType.LECTURE
//                            ? course.getTeacherReferences().get(0).getTeacherId()
//                            : course.getTeacherReferences().get(1).getTeacherId();

                    ClassSessionCreationRequest classSessionRequest = ClassSessionCreationRequest.builder()
                            .isException(false)
                            .timeSlotId(slot.getId())
                            .notes("Notes for test")
                            .replacedById(null)
                            .weekNumber(weekCount)
                            .status(ClassSessionStatus.SCHEDULED)
                            .sessionDate(currentDate.with(day))
                            .sessionType(classSessionType)
                            .roomId(41L)
                            .teacherId(1L)
                            .timetableId(timetable.getId())
                            .build();

                    theorySessionsThisWeek++;

                    classSessionService.createClassSession(classSessionRequest);
                    if (totalTheory > 0 && classSessionType == ClassSessionType.LECTURE) {
                        totalTheory--;
                    } else if (totalPractice > 0 && classSessionType == ClassSessionType.LAB) {
                        totalPractice--;
                    }

                    sessionsCreatedThisWeek++;
                    if (sessionsCreatedThisWeek >= perWeek) break;
                }
            }
            currentDate = currentDate.plusWeeks(1); // move to next week
        }
    }
}
