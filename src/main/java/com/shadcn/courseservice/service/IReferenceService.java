package com.shadcn.courseservice.service;

import com.shadcn.courseservice.dto.request.teacher.UpdateTeacherReferenceRequest;

import java.util.List;

public interface IReferenceService {
    void addATeacherReference(Long teacherId, Long departmentId);

    void deleteTeacherReferences(List<Long> referenceIds);

    void addAStudentReference(Long studentId, Long academicYearId, Long departmentId, String name);

    void deleteStudentReferences(List<Long> referenceIds);
    
    void updateTeacherReference(UpdateTeacherReferenceRequest updateTeacherReferenceRequest);
    
    
}
