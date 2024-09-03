package com.shadcn.courseservice.entity;

import java.util.List;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;

import lombok.*;
import lombok.experimental.FieldDefaults;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
@Entity
public class StudentReference extends BaseEntity {

    String name;

    @NotNull
    Long studentId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "department_id")
    Department department;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "academic_year_id")
    AcademicYear academicYear;

    @OneToMany(mappedBy = "student", fetch = FetchType.LAZY)
    List<Enrollment> enrollments;

    @ManyToMany(mappedBy = "students", fetch = FetchType.LAZY)
    List<Lesson> lessons;
}
