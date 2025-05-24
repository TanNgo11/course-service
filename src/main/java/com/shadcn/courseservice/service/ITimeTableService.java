package com.shadcn.courseservice.service;

import com.shadcn.courseservice.dto.response.timetable.TimetableResponse;

import java.time.LocalDate;
import java.util.List;

public interface ITimeTableService {
    void generateTimetablesForSemester(Long semesterId);

    List<TimetableResponse> getTimetableByCourseId(Long courseId);

    List<TimetableResponse> getTimetableByTeacherId(Long teacherId, Long semesterId);
    
    List<TimetableResponse> getTimetableByStudentIdAndSemesterId(Long studentId, Long semesterId);

    List<TimetableResponse> getTimetableByStudentIdAndDate(Long studentId, LocalDate date);
}
