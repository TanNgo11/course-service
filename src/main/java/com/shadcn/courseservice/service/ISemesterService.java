package com.shadcn.courseservice.service;

import java.util.List;

import com.shadcn.courseservice.dto.request.CourseIdsRequest;
import com.shadcn.courseservice.dto.response.*;

public interface ISemesterService {
    PageResponse<SemesterResponse> getAllSemesters(int current, int pageSize);

    void addOpenCoursesToSemester(List<Long> baseCourseIds, long semesterId);

    PageResponse<CourseResponse> getAllOpenCoursesInSemester(String semesterId, int current, int pageSize);

    PageResponse<CourseResponse> getAllCoursesInSemesterByDepartmentId(
            String semesterId, String departmentId, int current, int pageSize);

    void openRegistrationForSemester(long semesterId);

    void closeRegistrationForSemester(long semesterId);

    SemesterResponse getCurrentOpenSemester();

    void deleteCoursesByIds(CourseIdsRequest request);
}
