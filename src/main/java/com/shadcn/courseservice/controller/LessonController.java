package com.shadcn.courseservice.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.shadcn.courseservice.dto.request.Lesson.LessonAddRequest;
import com.shadcn.courseservice.dto.request.Lesson.UpdateLessonRequest;
import com.shadcn.courseservice.dto.request.LessonsDeleteRequest;
import com.shadcn.courseservice.dto.response.ApiResponse;
import com.shadcn.courseservice.dto.response.Lesson.LessonResponse;
import com.shadcn.courseservice.dto.response.PageResponse;
import com.shadcn.courseservice.service.impl.LessonService;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.http.MediaType;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

import static com.shadcn.courseservice.constant.PathConstant.API_V1_LESSONS;

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

    @GetMapping("/courses/{courseId}")
    @PreAuthorize("hasRole('ADMIN') or hasRole('TEACHER')")
    ApiResponse<List<LessonResponse>> getLessonsByCourseId(
            @PathVariable Long courseId
    ) {
        return ApiResponse.success(lessonService.getLessonsByCourseId(courseId));
    }

    @PutMapping(value = "/{lessonId}", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    @PreAuthorize("hasRole('ADMIN') or hasRole('TEACHER')")
    ApiResponse<Void> updateLessonById(
            @PathVariable Long lessonId,
            @RequestPart("request") String request,
            @RequestPart(value = "files", required = false) MultipartFile[] files
    ) throws JsonProcessingException {
        ObjectMapper objectMapper = new ObjectMapper();
        UpdateLessonRequest data = objectMapper.readValue(request, UpdateLessonRequest.class);
        if (files != null) {
            data.setFiles(files);
        }
        lessonService.updateLessonById(lessonId, data);
        return ApiResponse.empty();
    }
}
