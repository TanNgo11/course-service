package com.shadcn.courseservice.service;

import java.util.List;

import com.shadcn.courseservice.dto.request.course.CourseIdsRequest;
import com.shadcn.courseservice.dto.request.semester.SemesterCreationRequest;
import com.shadcn.courseservice.dto.response.*;
import com.shadcn.courseservice.dto.response.academicYear.SemesterResponse;
import com.shadcn.courseservice.dto.response.course.CourseResponse;
import com.shadcn.courseservice.entity.AcademicYear;
import com.shadcn.courseservice.entity.Semester;

public interface ISemesterService {
    PageResponse<SemesterResponse> getAllSemestersByAcademicYearId(int current, int pageSize, Long academicYearId);

    void addOpenCoursesToSemester(List<Long> baseCourseIds, long semesterId);

    void removeOpenCoursesFromSemester(List<Long> openingCourseIds, long semesterId);

    PageResponse<CourseResponse> getAllOpenCoursesInSemester(String semesterId, int current, int pageSize);

    PageResponse<CourseResponse> getAllCoursesInSemesterByDepartmentId(
            String semesterId, String departmentId, int current, int pageSize);

    void openRegistrationForSemester(long semesterId);

    void closeRegistrationForSemester(long semesterId);

    SemesterResponse getCurrentOpenSemester();

    void deleteCoursesByIds(CourseIdsRequest request);

    Semester createSemester(SemesterCreationRequest request);

    List<Semester> generateForOneYear(AcademicYear academicYear);

    void generateTimeTable(Long semesterId);
}
