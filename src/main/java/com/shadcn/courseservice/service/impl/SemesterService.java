package com.shadcn.courseservice.service.impl;

import java.time.LocalDate;
import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import com.shadcn.courseservice.dto.response.CourseResponse;
import com.shadcn.courseservice.dto.response.PageResponse;
import com.shadcn.courseservice.dto.response.SemesterResponse;
import com.shadcn.courseservice.entity.BaseCourse;
import com.shadcn.courseservice.entity.Course;
import com.shadcn.courseservice.entity.Registration;
import com.shadcn.courseservice.entity.Semester;
import com.shadcn.courseservice.mapper.CourseMapper;
import com.shadcn.courseservice.mapper.RegistrationMapper;
import com.shadcn.courseservice.mapper.SemesterMapper;
import com.shadcn.courseservice.repository.BaseCourseRepository;
import com.shadcn.courseservice.repository.CourseRepository;
import com.shadcn.courseservice.repository.RegistrationRepository;
import com.shadcn.courseservice.repository.SemesterRepository;
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

    @Override
    public PageResponse<SemesterResponse> getAllSemesters(int current, int pageSize) {
        Pageable pageable = PageRequest.of(current - 1, pageSize);
        Page<Semester> semesters = semesterRepository.findAll(pageable);

        return ConverToPaginationResponse.toPageResponse(semesters, semesterMapper::toSemesterResponse, current);
    }

    @Override
    public void addOpenCoursesToSemester(List<Long> baseCourseIds, long semesterId) {
        List<BaseCourse> baseCourses = baseCourseRepository.findAllById(baseCourseIds);
        Semester semester = semesterRepository
                .findById(semesterId)
                .orElseThrow(() -> new IllegalArgumentException("Semester not found"));
        for (BaseCourse baseCourse : baseCourses) {
            Course course =
                    Course.builder().baseCourse(baseCourse).semester(semester).build();

            courseRepository.save(course);
        }
    }

    @Override
    public PageResponse<CourseResponse> getAllOpenCoursesInSemester(long semesterId, int current, int pageSize) {
        Pageable pageable = PageRequest.of(current - 1, pageSize);
        Page<Course> courses = courseRepository.findBySemesterId(semesterId, pageable);

        return ConverToPaginationResponse.toPageResponse(courses, courseMapper::toCourseResponse, current);
    }

    @Override
    public void openRegistrationForSemester(long semesterId) {
        Semester semester = semesterRepository
                .findById(semesterId)
                .orElseThrow(() -> new IllegalArgumentException("Semester not found"));

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
                    .orElseThrow(() -> new IllegalArgumentException("Semester not found"));
        }

        List<Registration> registrations = registrationRepository.findAllRegistrationBySemesterId(semester.getId());
        registrationService.addStudentToCourse(registrations);

        if (semester.getRegistrationEndDate().isBefore(LocalDate.now())) {
            semester.setRegistrationOpen(false);
        }
        semesterRepository.save(semester);
    }
}
