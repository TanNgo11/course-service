package com.shadcn.courseservice.repository.custom.impl;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import com.querydsl.jpa.JPAExpressions;
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

        List<Course> totalCourses = queryFactory
                .selectFrom(course)
                .where(course.baseCourse
                        .departments
                        .any()
                        .id
                        .eq(Long.valueOf(departmentId))
                        .and(course.semester.id.eq(Long.valueOf(semesterId))))
                .fetch();

        // Fetch paginated results
        List<Course> courses = queryFactory
                .selectFrom(course)
                .where(course.baseCourse
                        .departments
                        .any()
                        .id
                        .eq(Long.valueOf(departmentId))
                        .and(course.semester.id.eq(Long.valueOf(semesterId))))
                .offset(pageable.getOffset())
                .limit(pageable.getPageSize())
                .fetch();

        return new PageImpl<>(courses, pageable, totalCourses.size());
    }

    @Override
    public Page<Course> findUnregisteredCoursesByDepartmentIdAndSemesterIdAndStudentId(
            String departmentId, String semesterId, String studentId, Pageable pageable) {
        QCourse course = QCourse.course;
        QRegistration registration = QRegistration.registration;

        List<Course> unregisteredCourses = queryFactory
                .selectFrom(course)
                .where(course.baseCourse
                        .departments
                        .any()
                        .id
                        .eq(Long.valueOf(departmentId))
                        .and(course.semester.id.eq(Long.valueOf(semesterId)))
                        .and(course.id.notIn(JPAExpressions.select(registration.course.id)
                                .from(registration)
                                .where(registration.studentProfile.studentId.eq(studentId)))))
                .offset(pageable.getOffset())
                .limit(pageable.getPageSize())
                .fetch();

        return new PageImpl<>(unregisteredCourses, pageable, unregisteredCourses.size());
    }

    @Override
    public Page<Course> findRegisteredCoursesByDepartmentIdAndSemesterIdAndStudentId(
            String departmentId, String semesterId, String studentId, Pageable pageable) {
        QRegistration registration = QRegistration.registration;

        List<Course> courses = queryFactory
                .select(registration.course)
                .from(registration)
                .where(registration
                        .course
                        .baseCourse
                        .departments
                        .any()
                        .id
                        .eq(Long.valueOf(departmentId))
                        .and(registration
                                .semester
                                .id
                                .eq(Long.valueOf(semesterId))
                                .and(registration.studentProfile.studentId.eq(studentId))))
                .offset(pageable.getOffset())
                .limit(pageable.getPageSize())
                .fetch();

        return new PageImpl<>(courses, pageable, courses.size());
    }

    @Override
    public Page<Course> findByDepartmentId(Long departmentId, Pageable pageable) {
        QCourse course = QCourse.course;
        List<Course> courses = queryFactory
                .selectFrom(course)
                .where(course.baseCourse.departments.any().id.eq(departmentId))
                .offset(pageable.getOffset())
                .limit(pageable.getPageSize())
                .fetch();

        return new PageImpl<>(courses, pageable, courses.size());
    }

    @Override
    public List<Course> findByDepartmentIdToList(Long departmentId) {
        QCourse course = QCourse.course;
        return queryFactory
                .selectFrom(course)
                .where(course.baseCourse.departments.any().id.eq(departmentId))
                .fetch();
    }

    @Override
    @Transactional
    public void removeOpeningCoursesFromSemester(Long semesterId, List<Long> courseIds) {
        QCourse course = QCourse.course;
        queryFactory
                .delete(course)
                .where(course.semester.id.eq(semesterId).and(course.id.in(courseIds)))
                .execute();
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
                .offset(pageable.getOffset())
                .limit(pageable.getPageSize())
                .fetch();

        return new PageImpl<>(baseCourses, pageable, baseCourses.size());
    }
}
