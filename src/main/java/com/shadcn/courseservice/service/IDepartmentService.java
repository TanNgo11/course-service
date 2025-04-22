package com.shadcn.courseservice.service;

import java.util.List;

import com.shadcn.courseservice.dto.response.PageResponse;
import com.shadcn.courseservice.dto.response.building.DepartmentResponse;
import com.shadcn.courseservice.dto.response.course.BaseCourseResponse;
import com.shadcn.courseservice.dto.response.course.CourseResponse;

public interface IDepartmentService {
    void addCoursesToDepartment(Long departmentId, List<Long> courseIds);

    void addBaseCoursesToDepartment(Long departmentId, List<Long> baseCourseIds);

    void removeBaseCoursesFromDepartment(Long departmentId, List<Long> baseCourseIds);

    PageResponse<CourseResponse> getCoursesByDepartment(Long departmentId, Integer current, Integer pageSize);

    PageResponse<BaseCourseResponse> getBaseCoursesByDepartment(Long departmentId, Integer current, Integer pageSize);

    PageResponse<DepartmentResponse> getAllDepartments(Integer current, Integer pageSize);

    PageResponse<BaseCourseResponse> getAllUnOpenedBaseCoursesByDepartment(
            Long semesterId, Long departmentId, Integer current, Integer pageSize);
}
