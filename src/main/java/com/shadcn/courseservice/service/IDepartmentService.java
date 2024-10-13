package com.shadcn.courseservice.service;

import java.util.List;

public interface IDepartmentService {
    void addSubjectsToDepartment(Long departmentId, List<Long> subjectIds);

    void removeSubjectsFromDepartment(Long departmentId, List<Long> subjectIds);
}
