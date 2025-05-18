package com.shadcn.courseservice.service.impl;

import java.time.DayOfWeek;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import org.springframework.stereotype.Service;

import com.shadcn.courseservice.dto.request.attendance.class_session.ClassSessionCreationRequest;
import com.shadcn.courseservice.entity.*;
import com.shadcn.courseservice.enums.ClassSessionStatus;
import com.shadcn.courseservice.enums.ClassSessionType;
import com.shadcn.courseservice.repository.TimeSlotRepository;
import com.shadcn.courseservice.repository.TimeTableRepository;
import com.shadcn.courseservice.service.IClassSessionService;
import com.shadcn.courseservice.service.IScheduleService;

import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;

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
                .students(new ArrayList<>(course.getStudentReferences()))
                .build();

        // Save the timetable to the database
        timeTableRepository.save(timetable);

        // Generate class sessions for the course
        generateTimeTableForCourse(course, timetable);
    }

    /*
     * Currently generating timetable for a course with 2 theory sessions and 1 practice session
     * In the same day of the week
     * */
    private void generateTimeTableForCourse(Course course, Timetable timetable) {
        BaseCourse baseCourse = course.getBaseCourse();
        int totalTheory = baseCourse.getNumsOfTheorySessions();
        int totalPractice = baseCourse.getNumsOfPracticeSessions();
        Set<DayOfWeek> days = timetable.getDaysOfWeek();

        LocalDate currentDate = course.getStartDate();
        List<TimeSlot> allTimeSlots = timeSlotRepository.findAll(); // existing in the database

        int weekCount = 0;
        while (totalTheory > 0 || totalPractice > 0) {
            weekCount++;
            int sessionsCreatedThisWeek = 0;
            int theorySessionsThisWeek = 0;
            for (DayOfWeek day : days) {


                for (TimeSlot slot : allTimeSlots) {
                    if (slot.getDayOfWeek() != day) continue;

                    var classSessionType =
                            theorySessionsThisWeek >= 2 ? ClassSessionType.LAB : ClassSessionType.LECTURE;
                    var teacherId = classSessionType == ClassSessionType.LECTURE
                            ? course.getTeacherReferences().get(0).getTeacherId()
                            : course.getTeacherReferences()
                                    .get(course.getTeacherReferences().size() - 1)
                                    .getTeacherId();

                    ClassSessionCreationRequest classSessionRequest = ClassSessionCreationRequest.builder()
                            .isException(false)
                            .timeSlotId(slot.getId())
                            .notes("Notes for test")
                            .replacedById(null)
                            .weekNumber(weekCount)
                            .status(ClassSessionStatus.SCHEDULED)
                            .sessionDate(currentDate.with(day))
                            .sessionType(classSessionType)
                            .roomId(5L) // this will be dynamic
                            .teacherId(10L) // this is for demo
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
                }
            }
            currentDate = currentDate.plusWeeks(1); // move to next week
        }
    }
}
