package com.shadcn.courseservice.service;

import java.util.List;

import com.shadcn.courseservice.dto.response.*;

public interface IReferenceService {
    void addATeacherReference(Long teacherId, Long courseId, Long departmentId);

    void deleteTeacherReferences(List<Long> referenceIds);

    void addAStudentReference(Long studentId, Long academicYearId, Long departmentId, String name);

    void deleteStudentReferences(List<Long> referenceIds);
}
