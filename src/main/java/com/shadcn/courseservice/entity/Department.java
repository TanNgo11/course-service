package com.shadcn.courseservice.entity;

import java.util.List;

import jakarta.persistence.Entity;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.JoinTable;
import jakarta.persistence.ManyToMany;

import lombok.*;
import lombok.experimental.FieldDefaults;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
@Entity
public class Department extends BaseEntity {

    String departmentName;

    @ManyToMany(mappedBy = "departments")
    List<AcademicYear> academicYears;

    @ManyToMany
    @JoinTable(
            name = "department_subject",
            joinColumns = @JoinColumn(name = "department_id"),
            inverseJoinColumns = @JoinColumn(name = "subject_id"))
    List<Subject> subjects;
}
