package com.shadcn.courseservice.entity;

import java.time.LocalDate;
import java.util.List;

import jakarta.persistence.*;

import com.fasterxml.jackson.annotation.JsonFormat;

import lombok.*;
import lombok.experimental.FieldDefaults;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
@Entity
public class AcademicYear extends BaseEntity {
    @JsonFormat(pattern = "dd-MM-yyyy")
    LocalDate startYear;

    @JsonFormat(pattern = "dd-MM-yyyy")
    LocalDate endYear;

    @ManyToMany(fetch = FetchType.LAZY)
    @JoinTable(
            name = "academic_year_department",
            joinColumns = @JoinColumn(name = "academic_year_id"),
            inverseJoinColumns = @JoinColumn(name = "department_id"))
    List<Department> departments;

    @OneToMany(mappedBy = "academicYear")
    List<Semester> semesters;
}
