package com.shadcn.courseservice.repository.custom;

import java.time.LocalDate;

public interface CustomAcademicYearRepository {
    boolean existsByStartYearAndEndYearInt(LocalDate startYear, LocalDate endYear);
}
