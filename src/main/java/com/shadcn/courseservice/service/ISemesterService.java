package com.shadcn.courseservice.service;

import java.util.List;

import com.shadcn.courseservice.dto.response.*;

public interface ISemesterService {
    PageResponse<SemesterResponse> getAllSemesters(int current, int pageSize);

    void addOpenCoursesToSemester(List<Long> baseCourseIds, long semesterId);

    PageResponse<CourseResponse> getAllOpenCoursesInSemester(long semesterId, int current, int pageSize);

    void openRegistrationForSemester(long semesterId);

    void closeRegistrationForSemester(long semesterId);
}
