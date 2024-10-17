package com.shadcn.courseservice.service;

import java.util.List;

import com.shadcn.courseservice.dto.response.CourseResponse;
import com.shadcn.courseservice.dto.response.PageResponse;

public interface IDepartmentService {
    void addCoursesToDepartment(Long departmentId, List<Long> courseIds);

    void removeCoursesFromDepartment(Long departmentId, List<Long> courseIds);

    PageResponse<CourseResponse> getCoursesByDepartment(Long departmentId, Integer current, Integer pageSize);
}
