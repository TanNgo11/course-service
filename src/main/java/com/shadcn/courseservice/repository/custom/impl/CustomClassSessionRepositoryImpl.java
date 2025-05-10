package com.shadcn.courseservice.repository.custom.impl;

import java.util.List;

import org.springframework.stereotype.Repository;

import com.querydsl.jpa.impl.JPAQueryFactory;
import com.shadcn.courseservice.entity.ClassSession;
import com.shadcn.courseservice.entity.QClassSession;
import com.shadcn.courseservice.repository.custom.CustomClassSessionRepository;

import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;

@Repository
@RequiredArgsConstructor
@FieldDefaults(level = lombok.AccessLevel.PRIVATE, makeFinal = true)
public class CustomClassSessionRepositoryImpl implements CustomClassSessionRepository {
    JPAQueryFactory queryFactory;

    @Override
    public List<ClassSession> findByCourseId(Long courseId) {
        QClassSession classSession = QClassSession.classSession;

        return queryFactory
                .selectFrom(classSession)
                .where(classSession.timetable.course.id.eq(courseId))
                .fetch();
    }
}
