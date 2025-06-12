package com.shadcn.courseservice.service.impl;

import java.util.*;
import java.util.stream.Collectors;

import com.shadcn.courseservice.dto.response.student.StudentProfileResponse;
import com.shadcn.courseservice.dto.response.user.UserProfileResponse;
import com.shadcn.courseservice.entity.Course;
import com.shadcn.courseservice.repository.CourseRepository;
import com.shadcn.courseservice.repository.httpClient.IdentityClient;
import com.shadcn.courseservice.repository.httpClient.ProfileClient;
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
    ProfileClient profileClient;
    private final CourseRepository courseRepository;
    private final IdentityClient identityClient;

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
                    "Attendance already exists for student {} in class session {}. Updating status to PRESENT.",
                    student.getId(),
                    classSession.getId());

            Attendance attendance = existingAttendance.get();
            attendance.setStatus(AttendanceStatus.PRESENT);
            attendance.setNotes(request.getNotes()); // Optional: update notes if needed
            return attendanceRepository.save(attendance);
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

//    @Override
//    public List<ClassSessionResponse> getClassSessionsByCourseId(Long courseId) {
//        // Get course and students
//        Course course = courseRepository.findById(courseId)
//                .orElseThrow(() -> new AppException(ErrorCode.COURSE_NOT_FOUND));
//        List<StudentReference> students = course.getStudentReferences();
//        List<Long> studentIds = students.stream()
//                .map(StudentReference::getStudentId)
//                .toList();
//
//        List<UserProfileResponse> studentProfileResponses = identityClient.getUserProfileResponses(studentIds).getResult();
//
//        // Map studentId → profile
//        Map<String, UserProfileResponse> profileMap = studentProfileResponses.stream()
//                .collect(Collectors.toMap(UserProfileResponse::getId, p -> p));
//
//        // Get sessions
//        List<ClassSession> sessions = customClassSessionRepository.findByCourseId(courseId);
//
//        // Map to response
//        List<ClassSessionResponse> classSessionResponses = new ArrayList<>();
//        for (ClassSession session : sessions) {
//            ClassSessionResponse response = classSessionMapper.toClassSessionResponse(session);
//
//            if (response.getAttendances().size() != students.size()) {
//                List<Attendance> newAttendances = createAttendances(students, session);
//                List<AttendanceResponse> mapped = newAttendances.stream()
//                        .map(attendanceMapper::toAttendanceResponse)
//                        .toList();
//
//                for (AttendanceResponse attendanceResponse : mapped) {
//                    UserProfileResponse studentProfile = profileMap.get(String.valueOf(attendanceResponse.getStudentId()));
//
//                    log.info("STUDENT PROFILE IN COURSE: {}", studentProfile.toString());
//
//                    attendanceResponse.setStudentName(
//                            studentProfile.getFirstName() + " " + studentProfile.getLastName()
//                    );
//                }
//                response.setAttendances(mapped);
//            }
//            classSessionResponses.add(response);
//        }
//        return classSessionResponses;
//    }

    @Override
    public List<ClassSessionResponse> getClassSessionsByCourseId(Long courseId) {
        // Get course and students
        Course course = courseRepository.findById(courseId)
                .orElseThrow(() -> new AppException(ErrorCode.COURSE_NOT_FOUND));
        List<StudentReference> students = course.getStudentReferences();

        // Get sessions
        List<ClassSession> sessions = customClassSessionRepository.findByCourseId(courseId);

        // Map to response
        List<ClassSessionResponse> responses = new ArrayList<>();
        for (ClassSession session : sessions) {
            ClassSessionResponse response = classSessionMapper.toClassSessionResponse(session);

            // Auto-create missing attendances
            if (response.getAttendances().size() != students.size()) {
                List<Attendance> newAttendances = createAttendances(students, session);
                List<AttendanceResponse> mapped = newAttendances.stream()
                        .map(attendanceMapper::toAttendanceResponse)
                        .toList();

                response.setAttendances(mapped);
            }

            responses.add(response);
        }

        return responses;
    }

    private List<Attendance> createAttendances(List<StudentReference> students, ClassSession classSession) {
        List<Attendance> attendances = students.stream()
                .map(student -> Attendance.builder()
                        .student(student)
                        .status(null)
                        .notes("Default note")
                        .classSession(classSession)
                        .build())
                .toList();

        return attendanceRepository.saveAll(attendances);
    }
}
