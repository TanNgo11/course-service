package com.shadcn.courseservice.service.impl;

import java.time.LocalDate;
import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import com.shadcn.courseservice.cronjob.CronSemester;
import com.shadcn.courseservice.dto.request.course.CourseIdsRequest;
import com.shadcn.courseservice.dto.response.PageResponse;
import com.shadcn.courseservice.dto.response.academicYear.SemesterResponse;
import com.shadcn.courseservice.dto.response.course.CourseResponse;
import com.shadcn.courseservice.entity.*;
import com.shadcn.courseservice.exception.AppException;
import com.shadcn.courseservice.exception.ErrorCode;
import com.shadcn.courseservice.mapper.CourseMapper;
import com.shadcn.courseservice.mapper.RegistrationMapper;
import com.shadcn.courseservice.mapper.SemesterMapper;
import com.shadcn.courseservice.repository.BaseCourseRepository;
import com.shadcn.courseservice.repository.CourseRepository;
import com.shadcn.courseservice.repository.RegistrationRepository;
import com.shadcn.courseservice.repository.SemesterRepository;
import com.shadcn.courseservice.service.IDepartmentService;
import com.shadcn.courseservice.service.IRegistrationService;
import com.shadcn.courseservice.service.ISemesterService;
import com.shadcn.courseservice.util.ConverToPaginationResponse;

import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;

@Service
@RequiredArgsConstructor
@Slf4j
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class SemesterService implements ISemesterService {
    SemesterRepository semesterRepository;
    BaseCourseRepository baseCourseRepository;
    RegistrationRepository registrationRepository;
    CourseRepository courseRepository;
    SemesterMapper semesterMapper;
    CourseMapper courseMapper;
    IRegistrationService registrationService;
    RegistrationMapper registrationMapper;
    IDepartmentService departmentService;
    private CronSemester cronSemester;

    @Override
    public PageResponse<SemesterResponse> getAllSemesters(int current, int pageSize) {
        Pageable pageable = PageRequest.of(current - 1, pageSize);
        Page<Semester> semesters = semesterRepository.findAll(pageable);
        cronSemester.updateSemesterStatus();
        return ConverToPaginationResponse.toPageResponse(semesters, semesterMapper::toSemesterResponse, current);
    }

    @Override
    public void addOpenCoursesToSemester(List<Long> baseCourseIds, long semesterId) {
        List<BaseCourse> baseCourses = baseCourseRepository.findAllById(baseCourseIds);
        Semester semester = semesterRepository
                .findById(semesterId)
                .orElseThrow(() -> new AppException(ErrorCode.SEMESTER_NOT_FOUND));

        for (BaseCourse baseCourse : baseCourses) {
            List<Department> departments = baseCourse.getDepartments();
            Course course = Course.builder()
                    .baseCourse(baseCourse)
                    .semester(semester)
                    // .departments(departments)
                    .startDate(semester.getStartDate())
                    .endDate(semester.getEndDate())
                    .remain(20)
                    .build();

            courseRepository.save(course);

            //            for (Department dep : departments) {
            //                log.info("Department: " + dep.getId());
            //                log.info("Course: " + course.getId());
            //
            //                departmentService.addCoursesToDepartment(dep.getId(), List.of(course.getId()));
            //            }
        }
    }

    @Override
    public void removeOpenCoursesFromSemester(List<Long> openingCourseIds, long semesterId) {
        courseRepository.removeOpeningCoursesFromSemester(semesterId, openingCourseIds);
    }

    @Override
    public PageResponse<CourseResponse> getAllOpenCoursesInSemester(String semesterId, int current, int pageSize) {
        Pageable pageable = PageRequest.of(current - 1, pageSize);
        Page<Course> courses = courseRepository.findBySemesterId(Long.parseLong(semesterId), pageable);

        return ConverToPaginationResponse.toPageResponse(courses, courseMapper::toCourseResponse, current);
    }

    @Override
    public PageResponse<CourseResponse> getAllCoursesInSemesterByDepartmentId(
            String semesterId, String departmentId, int current, int pageSize) {
        Pageable pageable = PageRequest.of(current - 1, pageSize);
        Page<Course> courses = courseRepository.findByDepartmentIdAndSemesterId(departmentId, semesterId, pageable);

        return ConverToPaginationResponse.toPageResponse(courses, courseMapper::toCourseResponse, current);
    }

    @Override
    public void openRegistrationForSemester(long semesterId) {
        Semester semester = semesterRepository
                .findById(semesterId)
                .orElseThrow(() -> new AppException(ErrorCode.SEMESTER_NOT_FOUND));

        semester.setRegistrationOpen(true);
        semesterRepository.save(semester);
    }

    @Override
    @Scheduled(cron = "0 0 0 * * *") // Run at midnight every day to close registration if the due date is passed
    public void closeRegistrationForSemester(long semesterId) {
        Semester semester;
        if (String.valueOf(semesterId) == null) {
            semester = semesterRepository.findByRegistrationOpen(true);
        } else {
            semester = semesterRepository
                    .findById(semesterId)
                    .orElseThrow(() -> new AppException(ErrorCode.SEMESTER_NOT_FOUND));
        }

        List<Registration> registrations = registrationRepository.findAllRegistrationBySemesterId(semester.getId());
        registrationService.addStudentToCourse(registrations);

        if (semester.getRegistrationEndDate().isBefore(LocalDate.now())) {
            semester.setRegistrationOpen(false);
        }
        semesterRepository.save(semester);
    }

    @Override
    public SemesterResponse getCurrentOpenSemester() {
        Semester semester = semesterRepository.findBySemesterActive(true);
        if (semester == null) {
            throw new AppException(ErrorCode.SEMESTER_NOT_FOUND);
        }
        return semesterMapper.toSemesterResponse(semester);
    }

    @Override
    public void deleteCoursesByIds(CourseIdsRequest request) {
        List<Long> courseIds = request.getCourseIds();
        List<Course> coursesToDelete = courseRepository.findAllById(courseIds);
        courseRepository.deleteAll(coursesToDelete);
    }
}
