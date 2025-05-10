package com.shadcn.courseservice.service;

import java.util.List;

import com.shadcn.courseservice.dto.request.attendance.StudentAttendanceRequest;
import com.shadcn.courseservice.dto.request.attendance.TeacherAttendanceRequest;
import com.shadcn.courseservice.dto.response.attendance.AttendanceResponse;
import com.shadcn.courseservice.dto.response.attendance.class_session.ClassSessionResponse;
import com.shadcn.courseservice.entity.Attendance;

public interface IAttendanceService {

    Attendance studentSelfAttendance(StudentAttendanceRequest request);

    List<Attendance> teacherCheckAttendanceForStudent(TeacherAttendanceRequest request);

    List<AttendanceResponse> getAttendancesByClassSession(Long classSessionId);

    List<AttendanceResponse> getAttendancesByStudent(Long studentId);

    List<ClassSessionResponse> getClassSessionsByCourseId(Long courseId);
}
