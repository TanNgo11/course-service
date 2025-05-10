package com.shadcn.courseservice.service.impl;

import org.springframework.stereotype.Service;

import com.shadcn.courseservice.dto.request.attendance.class_session.ClassSessionCreationRequest;
import com.shadcn.courseservice.entity.ClassSession;
import com.shadcn.courseservice.repository.*;
import com.shadcn.courseservice.service.IClassSessionService;

import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;

@Service
@RequiredArgsConstructor
@Slf4j
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class ClassSessionService implements IClassSessionService {
    TimeSlotRepository timeSlotRepository;
    TimeTableRepository timeTableRepository;
    RoomRepository roomRepository;
    TeacherReferenceRepository teacherReferenceRepository;
    StudentReferenceRepository studentReferenceRepository;
    ClassSessionRepository classSessionRepository;

    @Override
    public ClassSession createClassSession(ClassSessionCreationRequest request) {
        ClassSession classSession = ClassSession.builder()
                .status(request.getStatus())
                .sessionType(request.getSessionType())
                .sessionDate(request.getSessionDate())
                .weekNumber(request.getWeekNumber())
                .notes(request.getNotes())
                .isException(request.isException())
                .timeSlot(timeSlotRepository.getTimeSlotsById(request.getTimeSlotId()))
                .room(roomRepository.getRoomById(request.getRoomId()))
                .notes(request.getNotes())
                .timetable(timeTableRepository.getTimetableById(request.getTimetableId()))
                .teacher(teacherReferenceRepository.getReferenceById(request.getTeacherId()))
                .build();

        classSessionRepository.save(classSession);
        return classSession;
    }
}
