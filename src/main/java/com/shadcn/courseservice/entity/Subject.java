package com.shadcn.courseservice.entity;

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
public class Subject extends BaseEntity {

    String name;

    @ManyToMany(mappedBy = "subjects")
    List<Department> departments;

    @OneToMany(mappedBy = "subject", fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    List<Lesson> lessons;

    @ManyToMany(fetch = FetchType.LAZY)
    @JoinTable(
            name = "subject_semester",
            joinColumns = @JoinColumn(name = "subject_id"),
            inverseJoinColumns = @JoinColumn(name = "semester_id"))
    List<Semester> semesters;

    @OneToMany(mappedBy = "subject", fetch = FetchType.LAZY)
    List<Enrollment> enrollments;

    @ManyToMany(mappedBy = "subjects", fetch = FetchType.LAZY)
    List<TeacherReference> teacherReferences;
}
