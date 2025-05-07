package com.shadcn.courseservice.service;

import java.util.List;

import com.shadcn.courseservice.dto.request.attendance.StudentAttendanceRequest;
import com.shadcn.courseservice.dto.request.attendance.TeacherAttendanceRequest;
import com.shadcn.courseservice.entity.Attendance;

public interface IAttendanceService {

    Attendance studentSelfAttendance(StudentAttendanceRequest request);

    List<Attendance> teacherTakeAttendance(TeacherAttendanceRequest request);

    List<Attendance> getAttendancesByClassSession(Long classSessionId);

    List<Attendance> getAttendancesByStudent(Long studentId);
}
