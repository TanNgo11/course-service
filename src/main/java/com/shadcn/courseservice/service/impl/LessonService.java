package com.shadcn.courseservice.service.impl;

import com.shadcn.courseservice.dto.response.CourseResponse;
import com.shadcn.courseservice.dto.response.LessonResponse;
import com.shadcn.courseservice.dto.response.PageResponse;
import com.shadcn.courseservice.entity.*;
import com.shadcn.courseservice.exception.AppException;
import com.shadcn.courseservice.exception.ErrorCode;
import com.shadcn.courseservice.mapper.CourseMapper;
import com.shadcn.courseservice.repository.CourseRepository;
import com.shadcn.courseservice.repository.DepartmentRepository;
import com.shadcn.courseservice.repository.LessonRepository;
import com.shadcn.courseservice.repository.SemesterRepository;
import com.shadcn.courseservice.service.ICourseService;
import com.shadcn.courseservice.service.ILessonService;
import com.shadcn.courseservice.util.ConverToPaginationResponse;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class LessonService implements ILessonService {
    LessonRepository lessonRepository;
    CourseRepository courseRepository;

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
    public PageResponse<LessonResponse> getAllLessons(Integer current, Integer pageSize) {
        return null;
    }


    Course getCourse(Long courseId) {
        return courseRepository.findById(courseId).orElseThrow(() -> new AppException(ErrorCode.COURSE_NOT_FOUND));
    }
}
