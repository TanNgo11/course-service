package com.shadcn.courseservice.service.impl;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.shadcn.courseservice.dto.request.attendance.StudentAttendanceRequest;
import com.shadcn.courseservice.dto.request.attendance.TeacherAttendanceRequest;
import com.shadcn.courseservice.dto.response.attendance.AttendanceResponse;
import com.shadcn.courseservice.dto.response.attendance.class_session.ClassSessionResponse;
import com.shadcn.courseservice.entity.Attendance;
import com.shadcn.courseservice.entity.ClassSession;
import com.shadcn.courseservice.entity.StudentReference;
import com.shadcn.courseservice.enums.AttendanceStatus;
import com.shadcn.courseservice.exception.AppException;
import com.shadcn.courseservice.exception.ErrorCode;
import com.shadcn.courseservice.mapper.AttendanceMapper;
import com.shadcn.courseservice.mapper.ClassSessionMapper;
import com.shadcn.courseservice.repository.AttendanceRepository;
import com.shadcn.courseservice.repository.ClassSessionRepository;
import com.shadcn.courseservice.repository.StudentReferenceRepository;
import com.shadcn.courseservice.repository.custom.CustomClassSessionRepository;
import com.shadcn.courseservice.service.IAttendanceService;

import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;

@Service
@RequiredArgsConstructor
@Slf4j
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class AttendanceService implements IAttendanceService {

    AttendanceRepository attendanceRepository;
    ClassSessionRepository classSessionRepository;
    CustomClassSessionRepository customClassSessionRepository;
    StudentReferenceRepository studentReferenceRepository;
    AttendanceMapper attendanceMapper;
    ClassSessionMapper classSessionMapper;

    @Override
    @Transactional
    public Attendance studentSelfAttendance(StudentAttendanceRequest request) {
        log.info(
                "Student self-attendance: studentId={}, classSessionId={}",
                request.getStudentId(),
                request.getClassSessionId());

        // Get class session
        ClassSession classSession = classSessionRepository
                .findById(request.getClassSessionId())
                .orElseThrow(() -> new AppException(ErrorCode.CLASS_SESSION_NOT_FOUND));

        // Get student
        StudentReference student = studentReferenceRepository
                .findByStudentId(request.getStudentId())
                .orElseThrow(() -> new AppException(ErrorCode.STUDENT_NOT_FOUND));

        // Check if student is enrolled in the course
        if (!classSession.getTimetable().getStudents().contains(student)) {
            throw new AppException(ErrorCode.STUDENT_NOT_ENROLLED);
        }

        // Check if attendance already exists
        Optional<Attendance> existingAttendance =
                attendanceRepository.findByClassSessionAndStudent(classSession, student);
        if (existingAttendance.isPresent()) {
            log.info(
                    "Attendance already exists for student {} in class session {}",
                    student.getId(),
                    classSession.getId());
            return existingAttendance.get();
        }

        // Create new attendance record (default status is PRESENT)
        Attendance attendance = Attendance.builder()
                .classSession(classSession)
                .student(student)
                .status(AttendanceStatus.PRESENT)
                .notes(request.getNotes())
                .build();

        return attendanceRepository.save(attendance);
    }

    @Override
    @Transactional
    public List<Attendance> teacherCheckAttendanceForStudent(TeacherAttendanceRequest request) {
        log.info("Teacher taking attendance for class session: {}", request.getClassSessionId());
        // Get class session
        ClassSession classSession = classSessionRepository
                .findById(request.getClassSessionId())
                .orElseThrow(() -> new AppException(ErrorCode.CLASS_SESSION_NOT_FOUND));

        List<Attendance> attendances = new ArrayList<>();

        for (AttendanceResponse response : request.getAttendanceResponses()) {

            StudentReference student = studentReferenceRepository
                    .findByStudentId(response.getStudentId())
                    .orElseThrow(() -> new AppException(ErrorCode.STUDENT_NOT_FOUND));

            // Check if student is enrolled in the course
            if (!classSession.getTimetable().getStudents().contains(student)) {
                log.warn("Student {} is not enrolled in the course", student.getId());
                continue;
            }
            // Check if attendance already exists for student
            Optional<Attendance> existingAttendance =
                    attendanceRepository.findByClassSessionAndStudent(classSession, student);
            Attendance attendance;
            if (existingAttendance.isPresent()) {
                // Update existing attendance
                attendance = existingAttendance.get();
                attendance.setStatus(AttendanceStatus.valueOf(response.getStatus()));
                attendance.setNotes(response.getNotes());
            } else {
                // Create new attendance record
                attendance = Attendance.builder()
                        .classSession(classSession)
                        .student(student)
                        .status(AttendanceStatus.valueOf(response.getStatus()))
                        .notes(response.getNotes())
                        .build();
            }

            attendances.add(attendanceRepository.save(attendance));
        }
        return attendances;
    }

    @Override
    public List<AttendanceResponse> getAttendancesByClassSession(Long classSessionId) {
        return customClassSessionRepository.findAttendanceByClassSessionId(classSessionId).stream()
                .map(attendanceMapper::toAttendanceResponse)
                .toList();
    }

    @Override
    public List<AttendanceResponse> getAttendancesByStudent(Long studentId) {
        log.info("Getting attendances for student: {}", studentId);

        StudentReference student = studentReferenceRepository
                .findById(studentId)
                .orElseThrow(() -> new AppException(ErrorCode.STUDENT_NOT_FOUND));

        return attendanceRepository.findByStudent(student).stream()
                .map(attendanceMapper::toAttendanceResponse)
                .toList();
    }

    @Override
    public List<ClassSessionResponse> getClassSessionsByCourseId(Long courseId) {
        return customClassSessionRepository.findByCourseId(courseId).stream()
                .map(classSessionMapper::toClassSessionResponse)
                .toList();
    }
}
