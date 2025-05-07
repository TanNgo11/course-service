package com.shadcn.courseservice.controller;

import static com.shadcn.courseservice.constant.PathConstant.API_V1_ATTENDANCES;

import java.util.List;

import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import com.shadcn.courseservice.dto.request.attendance.StudentAttendanceRequest;
import com.shadcn.courseservice.dto.request.attendance.TeacherAttendanceRequest;
import com.shadcn.courseservice.dto.response.ApiResponse;
import com.shadcn.courseservice.entity.Attendance;
import com.shadcn.courseservice.service.IAttendanceService;

import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@RestController
@RequestMapping(API_V1_ATTENDANCES)
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class AttendanceController {

    IAttendanceService attendanceService;

    @PostMapping("/student")
    @PreAuthorize("hasRole('STUDENT')")
    public ApiResponse<Attendance> studentSelfAttendance(@RequestBody StudentAttendanceRequest request) {
        Attendance attendance = attendanceService.studentSelfAttendance(request);
        return ApiResponse.success(attendance);
    }

    @PostMapping("/teacher")
    @PreAuthorize("hasRole('TEACHER')")
    public ApiResponse<List<Attendance>> teacherTakeAttendance(@RequestBody TeacherAttendanceRequest request) {
        List<Attendance> attendances = attendanceService.teacherTakeAttendance(request);
        return ApiResponse.success(attendances);
    }

    @GetMapping("/class-session/{classSessionId}")
    @PreAuthorize("hasAnyRole('TEACHER', 'ADMIN')")
    public ApiResponse<List<Attendance>> getAttendancesByClassSession(@PathVariable Long classSessionId) {
        List<Attendance> attendances = attendanceService.getAttendancesByClassSession(classSessionId);
        return ApiResponse.success(attendances);
    }

    @GetMapping("/student/{studentId}")
    @PreAuthorize("hasAnyRole('STUDENT', 'TEACHER', 'ADMIN')")
    public ApiResponse<List<Attendance>> getAttendancesByStudent(@PathVariable Long studentId) {
        List<Attendance> attendances = attendanceService.getAttendancesByStudent(studentId);
        return ApiResponse.success(attendances);
    }
}
