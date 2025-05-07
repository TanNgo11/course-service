package com.shadcn.courseservice.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.querydsl.QuerydslPredicateExecutor;

import com.shadcn.courseservice.entity.AcademicYear;
import com.shadcn.courseservice.repository.custom.CustomAcademicYearRepository;

public interface AcademicYearRepository
        extends JpaRepository<AcademicYear, Long>,
                QuerydslPredicateExecutor<AcademicYear>,
                CustomAcademicYearRepository {

    // Get the last academic year
    Optional<AcademicYear> findTopByOrderByStartYearDesc();
}
