package com.shadcn.courseservice.controller;

import com.shadcn.courseservice.dto.request.LessonAddRequest;
import com.shadcn.courseservice.dto.request.LessonsDeleteRequest;
import com.shadcn.courseservice.dto.response.ApiResponse;
import com.shadcn.courseservice.service.ILessonService;
import com.shadcn.courseservice.service.impl.LessonService;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import static com.shadcn.courseservice.constant.PathConstant.API_V1_LESSONS;

@RestController
@RequestMapping(API_V1_LESSONS)
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class LessonController {

    private final LessonService lessonService;

    @PostMapping()
    @PreAuthorize("hasRole('ADMIN')")
    ApiResponse<Void> addALesson(@RequestBody LessonAddRequest request) {
        lessonService.addALesson(request.getCourseId(), request.getTitle());
        return ApiResponse.empty();
    }

    @DeleteMapping
    @PreAuthorize("hasRole('ADMIN')")
    ApiResponse<Void> deleteLessons(@RequestBody LessonsDeleteRequest request) {
        lessonService.deleteLessons(request.getLessonIds());
        return ApiResponse.empty();
    }
}
