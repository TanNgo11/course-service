package com.shadcn.courseservice.repository.custom.impl;

import java.time.LocalDate;

import org.springframework.stereotype.Repository;

import com.querydsl.jpa.impl.JPAQueryFactory;
import com.shadcn.courseservice.entity.QAcademicYear;
import com.shadcn.courseservice.repository.custom.CustomAcademicYearRepository;

import lombok.RequiredArgsConstructor;

@Repository
@RequiredArgsConstructor
public class CustomAcademicYearRepositoryImpl implements CustomAcademicYearRepository {
    private final JPAQueryFactory queryFactory;

    @Override
    public boolean existsByStartYearAndEndYearInt(LocalDate startYear, LocalDate endYear) {
        int startYearInt = startYear.getYear();
        int endYearInt = endYear.getYear();

        QAcademicYear academicYear = QAcademicYear.academicYear;

        long startYearCount = queryFactory
                .selectFrom(academicYear)
                .where(academicYear.startYear.year().eq(startYearInt))
                .fetchCount();

        if (startYearCount > 0) {
            return true;
        }

        long count = queryFactory
                .selectFrom(academicYear)
                .where(academicYear
                        .startYear
                        .year()
                        .eq(startYearInt)
                        .and(academicYear.endYear.year().eq(endYearInt)))
                .fetchCount();

        return count > 0;
    }
}
