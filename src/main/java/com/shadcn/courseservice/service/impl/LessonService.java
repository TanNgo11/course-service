package com.shadcn.courseservice.service.impl;

import com.shadcn.courseservice.dto.request.Lesson.UpdateLessonRequest;
import com.shadcn.courseservice.dto.response.FileUploadResponse;
import com.shadcn.courseservice.dto.response.Lesson.LessonResponse;
import com.shadcn.courseservice.dto.response.PageResponse;
import com.shadcn.courseservice.entity.Course;
import com.shadcn.courseservice.entity.Lesson;
import com.shadcn.courseservice.entity.LessonFile;
import com.shadcn.courseservice.exception.AppException;
import com.shadcn.courseservice.exception.ErrorCode;
import com.shadcn.courseservice.mapper.LessonMapper;
import com.shadcn.courseservice.repository.CourseRepository;
import com.shadcn.courseservice.repository.LessonRepository;
import com.shadcn.courseservice.repository.httpClient.FileServiceClient;
import com.shadcn.courseservice.service.ILessonService;
import com.shadcn.courseservice.util.ConverToPaginationResponse;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class LessonService implements ILessonService {
    LessonRepository lessonRepository;
    CourseRepository courseRepository;
    LessonMapper lessonMapper;
    FileServiceClient fileServiceClient;

    @Override
    @Transactional
    public void updateLessonById(Long lessonId, UpdateLessonRequest request) {
        Lesson lesson = lessonRepository.findById(lessonId)
                .orElseThrow(() -> new AppException(ErrorCode.LESSON_NOT_FOUND));
        lesson.setTitle(request.getTitle());
        lesson.setDescription(request.getDescription());
        lesson.setPublished(request.isPublished());
        if (request.getFiles() != null && request.getFiles().length > 0) {
            lesson.getFiles().clear(); 
            List<FileUploadResponse> fileUploadResponses = fileServiceClient.uploadMultipleFiles(request.getFiles()).getResult();
            for (FileUploadResponse fileUploadResponse : fileUploadResponses) {
                LessonFile lessonFile = LessonFile.builder()
                        .fileName(fileUploadResponse.getFileName())
                        .filePath(fileUploadResponse.getDownloadUri())
                        .lesson(lesson)
                        .build();
                lesson.getFiles().add(lessonFile);
            }
        }
        lessonRepository.save(lesson);
    }

    @Override
    @Transactional
    public void addALesson(Long courseId, String title) {
        Course course = getCourse(courseId);
        Lesson lesson = new Lesson();
        lesson.setTitle(title);
        lesson.setCourse(course);
        lessonRepository.save(lesson);
    }

    @Override
    @Transactional
    public void deleteLessons(List<Long> lessonIds) {
        List<Lesson> lessons = lessonRepository.findAllById(lessonIds);
        lessonRepository.deleteAll(lessons);
    }

    @Override
    @Transactional
    public List<LessonResponse> getLessonsByCourseId(Long courseId) {
        List<Lesson> lessons = lessonRepository.findAllByCourseId(courseId);
        var tenWeeks = 10;
        if (lessons.isEmpty()) {
            Course course = courseRepository.findById(courseId)
                    .orElseThrow(() -> new RuntimeException("Course not found with ID: " + courseId));

            lessons = new ArrayList<>();
            for (int i = 1; i <= tenWeeks; i++) {
                Lesson lesson = Lesson.builder()
                        .title("Week " + i)
                        .description("Lesson for Week " + i + " of the course")
                        .course(course)
                        .build();
                lessons.add(lesson);
            }

            lessons = lessonRepository.saveAll(lessons);
        }

        return lessonMapper.toLessonResponseList(lessons);
    }

    @Override
    public PageResponse<LessonResponse> getAllLessons(Integer current, Integer pageSize) {
        List<Lesson> allLessons = lessonRepository.findAll();
        Pageable pageable = PageRequest.of(current - 1, pageSize);
        Page<Lesson> lessonPage = new PageImpl<>(allLessons, pageable, allLessons.size());
        return ConverToPaginationResponse.toPageResponse(lessonPage, lessonMapper::toLessonResponse, current);
    }

    Course getCourse(Long courseId) {
        return courseRepository.findById(courseId).orElseThrow(() -> new AppException(ErrorCode.COURSE_NOT_FOUND));
    }
}
