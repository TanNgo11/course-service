package com.shadcn.courseservice.service;

import java.util.List;

import com.shadcn.courseservice.dto.response.LessonResponse;
import com.shadcn.courseservice.dto.response.PageResponse;

public interface ILessonService {
    void addALesson(Long courseId, String title);

    void deleteLessons(List<Long> lessonIds);

    PageResponse<LessonResponse> getAllLessons(Integer current, Integer pageSize);
}
