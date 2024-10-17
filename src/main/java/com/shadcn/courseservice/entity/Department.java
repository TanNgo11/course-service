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

    //    @ElementCollection
    //    @CollectionTable(
    //            name = "academic_year_department",
    //            joinColumns = @JoinColumn(name = "department_id"))
    //    @Column(name = "academic_year_id")
    //    List<Long> academicYearIds;

    @ManyToMany
    @JoinTable(
            name = "department_course",
            joinColumns = @JoinColumn(name = "department_id"),
            inverseJoinColumns = @JoinColumn(name = "course_id"))
    List<Course> courses;
}
