package com.shadcn.courseservice.service;

import com.shadcn.courseservice.dto.response.timetable.TimetableResponse;

import java.util.List;

public interface ITimeTableService {
    void generateTimetablesForSemester(Long semesterId);

    List<TimetableResponse> getTimetableByCourseId(Long courseId);

    List<TimetableResponse> getTimetableByTeacherId(Long teacherId, Long semesterId);
}
