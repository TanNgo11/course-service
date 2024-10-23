package com.shadcn.courseservice.service;

import com.shadcn.courseservice.dto.response.CourseResponse;
import com.shadcn.courseservice.dto.response.LessonResponse;
import com.shadcn.courseservice.dto.response.PageResponse;

import java.util.List;

public interface IReferenceService {
    void addATeacherReference(Long teacherId, Long courseId, Long departmentId);
    void deleteTeacherReferences(List<Long> referenceIds);
}
