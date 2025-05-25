package com.shadcn.courseservice.service;

import java.util.List;

import com.shadcn.courseservice.dto.response.timetable.TimetableResponse;

public interface ITimeTableService {
    void generateTimetablesForSemester(Long semesterId);

    List<TimetableResponse> getTimetableByCourseId(Long courseId);

    List<TimetableResponse> getTimetableByTeacherId(Long teacherId, Long semesterId);
}
