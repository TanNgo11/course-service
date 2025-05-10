package com.shadcn.courseservice.repository.custom;

import java.util.List;

import com.shadcn.courseservice.entity.ClassSession;

public interface CustomClassSessionRepository {
    List<ClassSession> findByCourseId(Long courseId);
}
