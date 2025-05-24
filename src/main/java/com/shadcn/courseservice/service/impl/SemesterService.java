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
import com.shadcn.courseservice.dto.request.semester.SemesterCreationRequest;
import com.shadcn.courseservice.dto.response.PageResponse;
import com.shadcn.courseservice.dto.response.academicYear.SemesterResponse;
import com.shadcn.courseservice.dto.response.course.CourseResponse;
import com.shadcn.courseservice.entity.*;
import com.shadcn.courseservice.enums.CourseStatus;
import com.shadcn.courseservice.exception.AppException;
import com.shadcn.courseservice.exception.ErrorCode;
import com.shadcn.courseservice.mapper.CourseMapper;
import com.shadcn.courseservice.mapper.RegistrationMapper;
import com.shadcn.courseservice.mapper.SemesterMapper;
import com.shadcn.courseservice.repository.*;
import com.shadcn.courseservice.service.*;
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
    AcademicYearRepository academicYearRepository;
    CourseRepository courseRepository;
    SemesterMapper semesterMapper;
    CourseMapper courseMapper;
    IRegistrationService registrationService;
    RegistrationMapper registrationMapper;
    IDepartmentService departmentService;
    ITimeSlotService timeSlotService;
    IScheduleService scheduleService;
    private CronSemester cronSemester;

    @Override
    public PageResponse<SemesterResponse> getAllSemestersByAcademicYearId(
            int current, int pageSize, Long academicYearId) {
        AcademicYear academicYear = academicYearRepository
                .findById(academicYearId)
                .orElseThrow(() -> new AppException(ErrorCode.ACADEMIC_YEAR_NOT_FOUND));

        Pageable pageable = PageRequest.of(current - 1, pageSize);
        Page<Semester> semesters = semesterRepository.findAllByAcademicYear(academicYear, pageable);
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
            //            Timetable timetable = Timetable.builder()
            //                    .
            //                    .build();
            Course course = Course.builder()
                    .baseCourse(baseCourse)
                    .semester(semester)
                    // .departments(departments)
                    // .timetables()
                    .processStatus(CourseStatus.IN_PROGRESS)
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
        semester = semesterRepository
                .findById(semesterId)
                .orElseThrow(() -> new AppException(ErrorCode.SEMESTER_NOT_FOUND));

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
    public SemesterResponse getCurrentOpenSemesterByDate() {
        LocalDate date = LocalDate.now();
        Semester semester = semesterRepository
                .findByDateWithinRange(date);
        
        return semesterMapper.toSemesterResponse(semester);
    }

    @Override
    public void deleteCoursesByIds(CourseIdsRequest request) {
        List<Long> courseIds = request.getCourseIds();
        List<Course> coursesToDelete = courseRepository.findAllById(courseIds);
        courseRepository.deleteAll(coursesToDelete);
    }

    @Override
    public Semester createSemester(SemesterCreationRequest request) {
        Semester semester = Semester.builder()
                .name(request.getName())
                .academicYear(request.getAcademicYear())
                .semesterActive(request.isSemesterActive())
                .registrationOpen(request.isRegistrationOpen())
                .registrationStartDate(request.getRegistrationStartDate())
                .registrationEndDate(request.getRegistrationEndDate())
                .startDate(request.getStartDate())
                .endDate(request.getEndDate())
                .courses(request.getCourses())
                .registrations(request.getRegistrations())
                .build();

        timeSlotService.initializeTimeSlots(semester.getStartDate(), semester.getEndDate());

        return semesterRepository.save(semester);
    }

    @Override
    public List<Semester> generateForOneYear(AcademicYear academicYear) {
        //        Optional<AcademicYear> previousAcademicYear = academicYearRepository.findTopByOrderByStartYearDesc();
        //        Optional<LocalDate> previousEndDate = previousAcademicYear.map(AcademicYear::getEndYear);
        //
        //        if (previousEndDate.isEmpty()) {
        //            // get first date of the last October
        //            previousEndDate =
        // Optional.of(academicYear.getStartYear().withMonth(10).withDayOfMonth(1).withYear(LocalDate.now().getYear() -
        // 1));
        //        }

        // Semester list
        String[] semesterNames = {"Spring", "Summer", "Fall", "Winter"};
        Semester[] semesters = new Semester[semesterNames.length];

        // Generate semesters for the current academic year (spring, summer, fall, winter)
        for (int i = 0; i < 4; i++) {
            LocalDate startDate = academicYear.getStartYear().plusMonths(i * 3);
            LocalDate endDate = academicYear.getStartYear().plusMonths((i + 1) * 3);
            LocalDate registrationEndDate = startDate.plusWeeks(2);

            SemesterCreationRequest semesterCreationRequest = SemesterCreationRequest.builder()
                    .name(semesterNames[i] + " " + academicYear.getStartYear().getYear())
                    .startDate(startDate)
                    .endDate(endDate)
                    .semesterActive(false)
                    .academicYear(academicYear)
                    .registrationStartDate(startDate)
                    .registrationEndDate(registrationEndDate)
                    .registrations(null)
                    .courses(null)
                    .registrationOpen(false)
                    .build();

            semesters[i] = this.createSemester(semesterCreationRequest);
        }

        return List.of(semesters);
    }

    @Override
    public void generateTimeTable(Long semesterId) {
        Semester semester = semesterRepository
                .findById(semesterId)
                .orElseThrow(() -> new AppException(ErrorCode.SEMESTER_NOT_FOUND));

        List<Course> courses = semester.getCourses();
        for (Course course : courses) {
            if (course.getTeacherReferences().isEmpty()) {
                throw new AppException(ErrorCode.TEACHER_NOT_FOUND);
            }
            scheduleService.generateTimeTable(course);
        }
    }
}
