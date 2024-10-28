package com.shadcn.courseservice.controller;

import static com.shadcn.courseservice.constant.PathConstant.API_V1_LESSONS;

import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import com.shadcn.courseservice.dto.request.LessonAddRequest;
import com.shadcn.courseservice.dto.request.LessonsDeleteRequest;
import com.shadcn.courseservice.dto.response.ApiResponse;
import com.shadcn.courseservice.dto.response.LessonResponse;
import com.shadcn.courseservice.dto.response.PageResponse;
import com.shadcn.courseservice.service.impl.LessonService;

import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;

@RestController
@RequestMapping(API_V1_LESSONS)
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class LessonController {

    LessonService lessonService;

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

    @GetMapping()
    @PreAuthorize("hasRole('ADMIN')")
    ApiResponse<PageResponse<LessonResponse>> getAllLessons(
            @RequestParam(defaultValue = "1", required = false) Integer current,
            @RequestParam(defaultValue = "10", required = false) Integer pageSize) {
        return ApiResponse.success(lessonService.getAllLessons(current, pageSize));
    }
}
