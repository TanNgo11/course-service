package com.shadcn.courseservice.service.impl;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import com.shadcn.courseservice.dto.request.registration.RegistrationTeacherRoleRequest;
import com.shadcn.courseservice.enums.TeacherRole;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.shadcn.courseservice.dto.response.*;
import com.shadcn.courseservice.dto.response.course.CourseResponse;
import com.shadcn.courseservice.dto.response.registration.RegistrationResponse;
import com.shadcn.courseservice.dto.response.student.StudentProfileResponse;
import com.shadcn.courseservice.dto.response.user.UserResponse;
import com.shadcn.courseservice.entity.*;
import com.shadcn.courseservice.enums.RegistrationStatus;
import com.shadcn.courseservice.exception.AppException;
import com.shadcn.courseservice.exception.ErrorCode;
import com.shadcn.courseservice.mapper.CourseMapper;
import com.shadcn.courseservice.mapper.RegistrationMapper;
import com.shadcn.courseservice.repository.*;
import com.shadcn.courseservice.repository.httpClient.IdentityClient;
import com.shadcn.courseservice.repository.httpClient.ProfileClient;
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
    RegistrationMapper registrationMapper;
    CourseMapper courseMapper;
    TeacherReferenceRepository teacherReferenceRepository;
    DepartmentRepository departmentRepository;
    StudentReferenceRepository studentReferenceRepository;
    IdentityClient identityClient;
    ProfileClient profileClient;
    AcademicYearRepository academicYearRepository;
    TeacherCourseRoleRepository teacherCourseRoleRepository;

    @Override
    @Transactional
    public void registerStudentToCourse(long studentId, List<Long> courseIds, long semesterId) {
        StudentProfileResponse studentProfile =
                identityClient.getStudentProfileById(studentId).getResult();
        Semester currentSemester = semesterRepository
                .findById(semesterId)
                .orElseThrow(() -> new AppException(ErrorCode.SEMESTER_NOT_FOUND));

        AcademicYear academicYear = academicYearRepository
                .findById(Long.valueOf(studentProfile.getAcademicYearId()))
                .orElseThrow(() -> new AppException(ErrorCode.ACADEMIC_YEAR_NOT_FOUND));

        Department department = departmentRepository
                .findById(Long.valueOf(studentProfile.getDepartmentId()))
                .orElseThrow(() -> new AppException(ErrorCode.DEPARTMENT_NOT_FOUND));

        StudentReference studentReference = studentReferenceRepository
                .findByStudentId(studentId)
                .orElseGet(() -> {
                    StudentReference newStudentReference = StudentReference.builder()
                            .studentId(studentId)
                            .department(department)
                            .academicYear(academicYear)
                            .build();
                    return studentReferenceRepository.save(newStudentReference);
                });

        LocalDate now = LocalDate.now();
        if (currentSemester.getStartDate().isAfter(now)
                || currentSemester.getEndDate().isBefore(now)) {
            throw new AppException(ErrorCode.INVALID_REGISTRATION_DATE);
        }

        for (Long courseId : courseIds) {
            if (registrationRepository.existsByStudentIdAndCourseIdAndSemesterId(studentId, courseId, semesterId)) {
                continue;
            }

            Course currentCourse =
                    courseRepository.findById(courseId).orElseThrow(() -> new AppException(ErrorCode.COURSE_NOT_FOUND));

            Registration registration = Registration.builder()
                    .studentReference(studentReference)
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
    @Transactional
    public void registerTeacherToCourse(
            long teacherId, List<Long> courseIds, long semesterId, long departmentId, String username) {
        Department department = departmentRepository
                .findById(departmentId)
                .orElseThrow(() -> new AppException(ErrorCode.DEPARTMENT_NOT_FOUND));

        Semester currentSemester = semesterRepository
                .findById(semesterId)
                .orElseThrow(() -> new AppException(ErrorCode.SEMESTER_NOT_FOUND));

        LocalDate now = LocalDate.now();
        if (currentSemester.getStartDate().isAfter(now)
                || currentSemester.getEndDate().isBefore(now)) {
            throw new AppException(ErrorCode.INVALID_REGISTRATION_DATE);
        }

        TeacherReference teacherProfile = teacherReferenceRepository
                .findByUsername(username)
                .orElseGet(() -> {
                    TeacherReference newTeacherReference = TeacherReference.builder()
                            .teacherId(teacherId)
                            .department(department)
                            .courses(new ArrayList<>())
                            .build();
                    return teacherReferenceRepository.save(newTeacherReference);
                });
        UserResponse user = identityClient.getUserDetailByUsername(username).getResult();
        teacherProfile.setTeacherId(user.getId());
        teacherProfile.setUsername(username);

        for (Long courseId : courseIds) {
            Course currentCourse =
                    courseRepository.findById(courseId).orElseThrow(() -> new AppException(ErrorCode.COURSE_NOT_FOUND));

            if (!currentCourse.getTeacherReferences().contains(teacherProfile)) {
                log.info("Adding teacher to course");
                    currentCourse.getTeacherReferences().add(teacherProfile);
                teacherProfile.getCourses().add(currentCourse);
            }
            courseRepository.save(currentCourse);
        }
        teacherReferenceRepository.save(teacherProfile);
    }

    @Override
    @Transactional
    public void registerTeacherRoleToCourse(RegistrationTeacherRoleRequest request) {
        TeacherReference teacherReference = teacherReferenceRepository
                .findByTeacherId(request.getTeacherId())
                .orElseThrow(() -> new AppException(ErrorCode.TEACHER_NOT_FOUND));

        Set<TeacherCourseRole> teacherCourseRoles = teacherReference.getTeacherCourseRoles();

        TeacherCourseRole teacherCourseRole = teacherCourseRoles.stream()
                .filter(tcr -> tcr.getCourse().getId().equals(request.getCourseId()))
                .findFirst()
                .orElseGet(() -> {
                    TeacherCourseRole newRole = TeacherCourseRole.builder()
                            .teacherReference(teacherReference)
                            .course(courseRepository.findById(request.getCourseId())
                                    .orElseThrow(() -> new AppException(ErrorCode.COURSE_NOT_FOUND)))
                            .roles(new HashSet<>())
                            .build();
                    teacherCourseRoles.add(newRole);
                    return newRole;
                });

        if (teacherCourseRole.getRoles().contains(request.getTeacherRole())) {
            throw new AppException(ErrorCode.TEACHER_ROLE_ALREADY_EXISTS);
        }

        teacherCourseRole.getRoles().add(request.getTeacherRole());
        teacherReferenceRepository.save(teacherReference);
    }

    @Override
    @Transactional
    public void unregisterTeacherFromCourse(long teacherId, List<Long> courseIds, long semesterId) {
        TeacherReference teacherProfile = teacherReferenceRepository
                .findByTeacherId(teacherId)
                .orElseThrow(() -> new AppException(ErrorCode.TEACHER_NOT_FOUND));

        Semester currentSemester = semesterRepository
                .findById(semesterId)
                .orElseThrow(() -> new AppException(ErrorCode.SEMESTER_NOT_FOUND));

        LocalDate now = LocalDate.now();
        if (currentSemester.getStartDate().isAfter(now)
                || currentSemester.getEndDate().isBefore(now)) {
            throw new AppException(ErrorCode.INVALID_REGISTRATION_DATE);
        }

        for (Long courseId : courseIds) {
            Course course =
                    courseRepository.findById(courseId).orElseThrow(() -> new AppException(ErrorCode.COURSE_NOT_FOUND));

            if (course.getTeacherReferences().contains(teacherProfile)) {
                log.info("Removing teacher from course");
                course.getTeacherReferences().remove(teacherProfile);
                teacherProfile.getCourses().remove(course);
            }

            courseRepository.save(course);
        }
        teacherReferenceRepository.save(teacherProfile);
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
    @Transactional
    public void approveStudentRegistration(List<Long> registrationIds, long semesterId) {
        List<Registration> registrations = registrationRepository.findAllRegistrationBySemesterId(semesterId);

        for (Registration registration : registrations) {
            if (registrationIds.contains(registration.getId())) {
                registration.setStatus(RegistrationStatus.APPROVED);
            }
        }
        registrationRepository.saveAll(registrations);
    }

    @Override
    public void addStudentToCourse(List<Registration> registrations) {
        for (Registration registration : registrations) {
            StudentReference studentReference = studentReferenceRepository
                    .findByStudentId(registration.getStudentReference().getStudentId())
                    .orElseThrow(() -> new AppException(ErrorCode.STUDENT_NOT_FOUND));
            Course course =
                    courseRepository.findById(registration.getCourse().getId()).get();
            Semester semester = semesterRepository
                    .findById(registration.getSemester().getId())
                    .get();
            if (studentReference == null) {
                throw new AppException(ErrorCode.STUDENT_NOT_FOUND);
            }
            if (course == null) {
                throw new AppException(ErrorCode.COURSE_NOT_FOUND);
            }
            if (semester == null) {
                throw new AppException(ErrorCode.SEMESTER_NOT_FOUND);
            }

            course.getStudentReferences().add(studentReference);
            registration.setStatus(RegistrationStatus.APPROVED);
            registrationRepository.save(registration);
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
