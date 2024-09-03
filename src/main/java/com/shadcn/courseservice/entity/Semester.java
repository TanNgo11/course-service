package com.shadcn.courseservice.entity;

import java.time.LocalDate;
import java.util.List;

import jakarta.persistence.Entity;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToMany;
import jakarta.persistence.ManyToOne;

import lombok.*;
import lombok.experimental.FieldDefaults;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
@Entity
public class Semester extends BaseEntity {

    String name;

    LocalDate startDate;

    LocalDate endDate;

    @ManyToOne
    @JoinColumn(name = "academic_year_id")
    AcademicYear academicYear;

    @ManyToMany(mappedBy = "semesters")
    List<Subject> subjects;
}
