package com.shadcn.courseservice.controller;

import com.shadcn.courseservice.dto.response.ApiResponse;
import com.shadcn.courseservice.dto.response.timetable.TimetableResponse;
import com.shadcn.courseservice.service.ITimeTableService;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;

import static com.shadcn.courseservice.constant.PathConstant.API_V1_TIME_TABLES;

@RestController
@RequiredArgsConstructor
@RequestMapping(API_V1_TIME_TABLES)
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@PreAuthorize("hasRole('ADMIN')")
public class TimeTableController {
    ITimeTableService timeTableService;

    @PostMapping("/semesters/{semesterId}")
    public ApiResponse<Void> initialTimeTableForSemester(@PathVariable Long semesterId) {
        timeTableService.generateTimetablesForSemester(semesterId);
        return ApiResponse.empty();
    }

    @GetMapping("/courses/{courseId}")
    public ApiResponse<List<TimetableResponse>> getTimetableByCourseId(@PathVariable Long courseId) {
        return ApiResponse.success(timeTableService.getTimetableByCourseId(courseId));
    }

    @GetMapping("/teachers/{teacherId}/semesters/{semesterId}")
    @PreAuthorize("hasRole('ADMIN') or hasRole('TEACHER')")
    public ApiResponse<List<TimetableResponse>> getTimetableByTeacherId(@PathVariable Long teacherId, @PathVariable Long semesterId) {
        return ApiResponse.success(timeTableService.getTimetableByTeacherId(teacherId, semesterId));
    }
    
    @GetMapping("/students/{studentId}/semesters/{semesterId}")
    @PreAuthorize("hasRole('STUDENT') or hasRole('ADMIN')")
    public ApiResponse<List<TimetableResponse>> getTimetableByStudentIdAndSemesterId(@PathVariable Long studentId, @PathVariable Long semesterId) {
        return ApiResponse.success(timeTableService.getTimetableByStudentIdAndSemesterId(studentId, semesterId));
    }

    @GetMapping("/students/{studentId}")
    @PreAuthorize("hasRole('STUDENT') or hasRole('ADMIN')")
    public ApiResponse<List<TimetableResponse>> getTimetableByStudentIdAndDate(@PathVariable Long studentId, @RequestParam LocalDate date) {
        return ApiResponse.success(timeTableService.getTimetableByStudentIdAndDate(studentId, date));
    }
    

}
