package com.shadcn.courseservice.entity;

import java.time.LocalDate;
import java.util.List;

import jakarta.persistence.*;

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

    // Map to AcademicYear
    @ManyToOne
    @JoinColumn(name = "academic_year_id")
    AcademicYear academicYear;

    @ManyToMany(mappedBy = "semesters")
    List<Subject> subjects;

    //    @ElementCollection
    //    @CollectionTable(
    //            name = "semester_subject",
    //            joinColumns = @JoinColumn(name = "semester_id"))
    //    @Column(name = "subject_id")
    //    List<Long> subjectIds;
}
