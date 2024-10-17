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
public class Course extends BaseEntity {

    String name;

    @ElementCollection
    List<String> studentIds;

    @ElementCollection
    List<String> teacherIds;

    @ManyToMany(mappedBy = "courses", fetch = FetchType.LAZY)
    List<Department> departments;

    //    @ElementCollection
    //    @CollectionTable(
    //            name = "department_subject",
    //            joinColumns = @JoinColumn(name = "subject_id"))
    //    @Column(name = "department_id")
    //    List<Long> departmentIds;

    @OneToMany(mappedBy = "course", fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    List<Lesson> lessons;

    @ManyToMany(fetch = FetchType.LAZY)
    @JoinTable(
            name = "course_semester",
            joinColumns = @JoinColumn(name = "course_id"),
            inverseJoinColumns = @JoinColumn(name = "semester_id"))
    List<Semester> semesters;

    @OneToMany(mappedBy = "course", fetch = FetchType.LAZY)
    List<Enrollment> enrollments;

    @ManyToMany(mappedBy = "courses", fetch = FetchType.LAZY)
    List<TeacherReference> teacherReferences;
}
