package com.shadcn.courseservice.service.impl;

import java.time.LocalDate;
import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import com.shadcn.courseservice.dto.response.PageResponse;
import com.shadcn.courseservice.dto.response.RegistrationResponse;
import com.shadcn.courseservice.entity.Course;
import com.shadcn.courseservice.entity.Registration;
import com.shadcn.courseservice.entity.Semester;
import com.shadcn.courseservice.entity.StudentProfile;
import com.shadcn.courseservice.enums.RegistrationStatus;
import com.shadcn.courseservice.exception.AppException;
import com.shadcn.courseservice.exception.ErrorCode;
import com.shadcn.courseservice.mapper.ProfileMapper;
import com.shadcn.courseservice.mapper.RegistrationMapper;
import com.shadcn.courseservice.repository.CourseRepository;
import com.shadcn.courseservice.repository.RegistrationRepository;
import com.shadcn.courseservice.repository.SemesterRepository;
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

    @Override
    public RegistrationResponse registerStudentToCourse(long studentId, long courseId, long semesterId) {
        StudentProfile studentProfile = profileMapper.toStudentProfile(
                profileService.getPublicStudentProfiles(new long[] {studentId}).get(0));
        Course currentCourse = courseRepository.findById(courseId).get();
        Semester currentSemester = semesterRepository.findById(semesterId).get();
        if (studentProfile == null) {
            throw new AppException(ErrorCode.STUDENT_NOT_FOUND);
        }
        if (currentCourse == null) {
            throw new AppException(ErrorCode.COURSE_NOT_FOUND);
        }
        if (currentSemester == null) {
            throw new AppException(ErrorCode.SEMESTER_NOT_FOUND);
        }
        if (currentSemester.getStartDate().isAfter(LocalDate.now())
                || currentSemester.getEndDate().isBefore(LocalDate.now())) {
            throw new AppException(ErrorCode.INVALID_REGISTRATION_DATE);
        }

        Registration registration = Registration.builder()
                .studentProfile(studentProfile)
                .course(currentCourse)
                .registrationDate(LocalDate.now())
                .semester(currentSemester)
                .status(RegistrationStatus.PENDING)
                .cancellationDeadline(currentSemester.getRegistrationEndDate())
                .build();
        registrationRepository.save(registration);

        return registrationMapper.toRegistrationResponse(registration);
    }

    @Override
    public void unregisterStudentsFromCourseForStudent(long courseId, long studentId) {
        Registration registration = registrationRepository.findByStudentIdAndCourseId(studentId, courseId);
        if (registration == null) {
            throw new AppException(ErrorCode.REGISTRATION_NOT_FOUND);
        }

        if (registration.getCancellationDeadline() != null
                && registration.getCancellationDeadline().isBefore(LocalDate.now())) {
            throw new AppException(ErrorCode.REGISTRATION_CANCELLATION_DEADLINE_PASSED);
        }

        registrationRepository.delete(registration);
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
        StudentProfile studentProfile = profileMapper.toStudentProfile(
                profileService.getPublicStudentProfiles(new long[] {studentId}).get(0));
        if (studentProfile == null) {
            throw new AppException(ErrorCode.STUDENT_NOT_FOUND);
        }
        Pageable pageable = PageRequest.of(current - 1, pageSize);
        Page<Registration> registrations = registrationRepository.findAllByStudentId(studentId, pageable);

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

    //    public void addStudentsToCourseInSemester() {
    //        Semester semester = semesterRepository.findByRegistrationOpen(true);
    //        List<Registration> registrations =
    // registrationRepository.findAllRegistrationBySemesterId(semester.getId());
    //        addStudentToCourse(registrationMapper.toRegistrationResponseList(registrations));
    //    }
}
