package com.shadcn.courseservice.repository.custom.impl;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Repository;

import com.querydsl.jpa.impl.JPAQueryFactory;
import com.shadcn.courseservice.entity.*;
import com.shadcn.courseservice.repository.custom.CustomCourseRepository;

import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;

@FieldDefaults(level = lombok.AccessLevel.PRIVATE, makeFinal = true)
@Repository
@RequiredArgsConstructor
public class CustomCourseRepositoryImpl implements CustomCourseRepository {
    JPAQueryFactory queryFactory;

    @Override
    public Page<Course> findByDepartmentIdAndSemesterId(String departmentId, String semesterId, Pageable pageable) {
        QCourse course = QCourse.course;

        List<Course> courses = queryFactory
                .selectFrom(course)
                .where(course.baseCourse
                        .departments
                        .any()
                        .id
                        .eq(Long.valueOf(departmentId))
                        .and(course.semester.id.eq(Long.valueOf(semesterId))))
                .fetch();

        return new PageImpl<>(courses, pageable, courses.size());
    }

    @Override
    public Page<Course> findByDepartmentId(Long departmentId, Pageable pageable) {
        QCourse course = QCourse.course;
        List<Course> courses = queryFactory
                .selectFrom(course)
                .where(course.baseCourse.departments.any().id.eq(departmentId))
                .fetch();

        return new PageImpl<>(courses, pageable, courses.size());
    }

    @Override
    public Page<BaseCourse> findOpenCoursesBySemesterAdnDepartment(
            Long semesterId, Long departmentId, Pageable pageable) {
        QBaseCourse baseCourse = QBaseCourse.baseCourse;
        QCourse course = QCourse.course;

        List<BaseCourse> baseCourses = queryFactory
                .selectFrom(baseCourse)
                .leftJoin(baseCourse.courses, course)
                .where(baseCourse
                        .departments
                        .any()
                        .id
                        .eq(departmentId)
                        .and(course.semester.id.ne(semesterId).or(course.semester.id.isNull())))
                .distinct()
                .fetch();

        return new PageImpl<>(baseCourses, pageable, baseCourses.size());
    }
}
