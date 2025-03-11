package com.shadcn.courseservice.service.impl;

import java.time.LocalDate;
import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.shadcn.courseservice.dto.response.CourseResponse;
import com.shadcn.courseservice.dto.response.PageResponse;
import com.shadcn.courseservice.dto.response.RegistrationResponse;
import com.shadcn.courseservice.entity.*;
import com.shadcn.courseservice.enums.RegistrationStatus;
import com.shadcn.courseservice.exception.AppException;
import com.shadcn.courseservice.exception.ErrorCode;
import com.shadcn.courseservice.mapper.CourseMapper;
import com.shadcn.courseservice.mapper.ProfileMapper;
import com.shadcn.courseservice.mapper.RegistrationMapper;
import com.shadcn.courseservice.repository.*;
import com.shadcn.courseservice.service.IProfileService;
import com.shadcn.courseservice.service.IRegistrationService;
import com.shadcn.courseservice.util.ConverToPaginationResponse;

import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;

@Service
@RequiredArgsConstructor
@Slf4j
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class RegistrationService implements IRegistrationService {
    RegistrationRepository registrationRepository;
    SemesterRepository semesterRepository;
    CourseRepository courseRepository;
    IProfileService profileService;
    ProfileMapper profileMapper;
    RegistrationMapper registrationMapper;
    StudentProfileRepository studentProfileRepository;
    TeacherProfileRepository teacherProfileRepository;
    CourseMapper courseMapper;

    @Override
    @Transactional
    public void registerStudentToCourse(long studentId, List<Long> courseIds, long semesterId) {
        // Fetch or create StudentProfile
        StudentProfile studentProfile = studentProfileRepository.getStudentProfileByStudentId(studentId);

        if (studentProfile == null) {
            studentProfile =
                    profileMapper.toStudentProfile(profileService.getStudentProfileByStudentEntityId(studentId));
            studentProfileRepository.save(studentProfile);
        }

        Semester currentSemester = semesterRepository
                .findById(semesterId)
                .orElseThrow(() -> new AppException(ErrorCode.SEMESTER_NOT_FOUND));

        LocalDate now = LocalDate.now();
        if (currentSemester.getStartDate().isAfter(now)
                || currentSemester.getEndDate().isBefore(now)) {
            throw new AppException(ErrorCode.INVALID_REGISTRATION_DATE);
        }

        for (Long courseId : courseIds) {

            // Check if the student is already registered for the course
            if (registrationRepository.existsByStudentIdAndCourseIdAndSemesterId(studentId, courseId, semesterId)) {
                continue;
            }

            Course currentCourse =
                    courseRepository.findById(courseId).orElseThrow(() -> new AppException(ErrorCode.COURSE_NOT_FOUND));

            // Create and save Registration
            Registration registration = Registration.builder()
                    .studentProfile(studentProfile)
                    .studentId(String.valueOf(studentId))
                    .course(currentCourse)
                    .registrationDate(now)
                    .semester(currentSemester)
                    .status(RegistrationStatus.PENDING)
                    .cancellationDeadline(currentSemester.getRegistrationEndDate())
                    .build();

            registrationRepository.save(registration);
        }
    }

    @Override
    public void registerTeacherToCourse(long teacherId, List<Long> courseIds, long semesterId) {
        TeacherProfile teacherProfile = teacherProfileRepository.getTeacherProfileByTeacherId(teacherId);

        if (teacherProfile == null) {
            teacherProfile =
                    profileMapper.toTeacherProfile(profileService.getTeacherProfileByTeacherEntityId(teacherId));
            teacherProfileRepository.save(teacherProfile);
        }

        Semester currentSemester = semesterRepository
                .findById(semesterId)
                .orElseThrow(() -> new AppException(ErrorCode.SEMESTER_NOT_FOUND));

        LocalDate now = LocalDate.now();
        if (currentSemester.getStartDate().isAfter(now)
                || currentSemester.getEndDate().isBefore(now)) {
            throw new AppException(ErrorCode.INVALID_REGISTRATION_DATE);
        }

        for (Long courseId : courseIds) {
            Course currentCourse =
                    courseRepository.findById(courseId).orElseThrow(() -> new AppException(ErrorCode.COURSE_NOT_FOUND));

            if (!currentCourse.getTeacherIds().contains(String.valueOf(teacherId))) {
                currentCourse.getTeacherIds().add(String.valueOf(teacherId));
            }
            courseRepository.save(currentCourse);
        }
    }

    @Override
    public void unregisterTeacherFromCourse(long teacherId, List<Long> courseId, long semesterId) {
        TeacherProfile teacherProfile = teacherProfileRepository.getTeacherProfileByTeacherId(teacherId);

        if (teacherProfile == null) {
            teacherProfile =
                    profileMapper.toTeacherProfile(profileService.getTeacherProfileByTeacherEntityId(teacherId));
            teacherProfileRepository.save(teacherProfile);
        }

        Semester currentSemester = semesterRepository
                .findById(semesterId)
                .orElseThrow(() -> new AppException(ErrorCode.SEMESTER_NOT_FOUND));

        LocalDate now = LocalDate.now();
        if (currentSemester.getStartDate().isAfter(now)
                || currentSemester.getEndDate().isBefore(now)) {
            throw new AppException(ErrorCode.INVALID_REGISTRATION_DATE);
        }

        for (Long id : courseId) {
            Course course = courseRepository.findById(id).isPresent()
                    ? courseRepository.findById(id).get()
                    : null;
            if (course == null) {
                throw new AppException(ErrorCode.COURSE_NOT_FOUND);
            }
            course.getTeacherIds().remove(String.valueOf(teacherId));
            courseRepository.save(course);
        }
    }

    @Override
    public void unregisterStudentsFromCourseForStudent(long studentId, List<String> courseCodes, long semesterId) {
        for (String code : courseCodes) {
            Registration registration = registrationRepository.findByRegistrationByStudentIdAndCourseCodeAndSemesterId(
                    studentId, code, semesterId);
            if (registration == null) {
                throw new AppException(ErrorCode.REGISTRATION_NOT_FOUND);
            }
            registrationRepository.delete(registration);
        }
    }

    @Override
    public void unregisterStudentsFromCourseForAdmin(long courseId, long studentId) {
        Registration registration = registrationRepository.findByStudentIdAndCourseId(studentId, courseId);
        if (registration == null) {
            throw new AppException(ErrorCode.REGISTRATION_NOT_FOUND);
        }

        registrationRepository.delete(registration);
    }

    @Override
    public void unregisterAllCoursesFromStudent(long studentId) {
        registrationRepository.deleteAllByStudentId(studentId);
    }

    @Override
    public PageResponse<RegistrationResponse> getAllRegistrationsForStudent(long studentId, int current, int pageSize) {
        Pageable pageable = PageRequest.of(current - 1, pageSize);

        Page<Registration> registrations = registrationRepository.findAllByStudentProfileId(studentId, pageable);

        return ConverToPaginationResponse.toPageResponse(
                registrations, registrationMapper::toRegistrationResponse, current);
    }

    @Override
    public PageResponse<RegistrationResponse> getAllRegistrationsForCourse(long courseId, int current, int pageSize) {
        Course course = courseRepository.findById(courseId).get();
        if (course == null) {
            throw new AppException(ErrorCode.COURSE_NOT_FOUND);
        }
        Pageable pageable = PageRequest.of(current - 1, pageSize);
        Page<Registration> registrations = registrationRepository.findAllByCourseId(courseId, pageable);

        return ConverToPaginationResponse.toPageResponse(
                registrations, registrationMapper::toRegistrationResponse, current);
    }

    @Override
    public PageResponse<RegistrationResponse> getAllRegistrationsForSemester(
            long semesterId, int current, int pageSize) {
        Semester semester = semesterRepository.findById(semesterId).get();
        if (semester == null) {
            throw new AppException(ErrorCode.SEMESTER_NOT_FOUND);
        }
        Pageable pageable = PageRequest.of(current - 1, pageSize);
        Page<Registration> registrations = registrationRepository.findAllBySemesterId(semesterId, pageable);

        return ConverToPaginationResponse.toPageResponse(
                registrations, registrationMapper::toRegistrationResponse, current);
    }

    @Override
    public void addStudentToCourse(List<Registration> registrations) {
        for (Registration registration : registrations) {
            StudentProfile studentProfile = profileMapper.toStudentProfile(profileService
                    .getPublicStudentProfiles(new long[] {
                        Long.valueOf(registration.getStudentProfile().getStudentId())
                    })
                    .get(0));
            Course course =
                    courseRepository.findById(registration.getCourse().getId()).get();
            Semester semester = semesterRepository
                    .findById(registration.getSemester().getId())
                    .get();
            if (studentProfile == null) {
                throw new AppException(ErrorCode.STUDENT_NOT_FOUND);
            }
            if (course == null) {
                throw new AppException(ErrorCode.COURSE_NOT_FOUND);
            }
            if (semester == null) {
                throw new AppException(ErrorCode.SEMESTER_NOT_FOUND);
            }

            course.getStudentIds().add(String.valueOf(studentProfile.getId()));
            registration.setStatus(RegistrationStatus.APPROVED);
        }
    }

    @Override
    public PageResponse<RegistrationResponse> getRegistrationsByStudentIdAndSemesterId(
            long studentId, long semesterId, int current, int pageSize) {
        Pageable pageable = PageRequest.of(current - 1, pageSize);
        Page<Registration> registrations =
                registrationRepository.getRegistrationByStudentIdAndSemesterId(studentId, semesterId, pageable);

        return ConverToPaginationResponse.toPageResponse(
                registrations, registrationMapper::toRegistrationResponse, current);
    }

    @Override
    public PageResponse<CourseResponse> getAllUnregisteredCoursesInSemesterByDepartmentForStudent(
            String studentId, String semesterId, String departmentId, int current, int pageSize) {
        Pageable pageable = PageRequest.of(current - 1, pageSize);

        Page<Course> allUnregisteredCoursesPage =
                courseRepository.findUnregisteredCoursesByDepartmentIdAndSemesterIdAndStudentId(
                        departmentId, semesterId, studentId, pageable);

        return ConverToPaginationResponse.toPageResponse(
                allUnregisteredCoursesPage, courseMapper::toCourseResponse, current);
    }

    @Override
    public PageResponse<CourseResponse> getAllRegisteredCoursesInSemesterByDepartmentForStudent(
            String studentId, String semesterId, String departmentId, int current, int pageSize) {
        Pageable pageable = PageRequest.of(current - 1, pageSize);

        Page<Course> courses = courseRepository.findRegisteredCoursesByDepartmentIdAndSemesterIdAndStudentId(
                departmentId, semesterId, studentId, pageable);

        return ConverToPaginationResponse.toPageResponse(courses, courseMapper::toCourseResponse, current);
    }
}
