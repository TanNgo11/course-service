package com.shadcn.courseservice.repository.custom;

import java.util.List;

import com.shadcn.courseservice.entity.Attendance;
import com.shadcn.courseservice.entity.ClassSession;

public interface CustomClassSessionRepository {
    List<ClassSession> findByCourseId(Long courseId);

    List<Attendance> findAttendanceByClassSessionId(Long classSessionId);
}
