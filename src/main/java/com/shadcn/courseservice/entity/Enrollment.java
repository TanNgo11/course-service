package com.shadcn.courseservice.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.JoinColumn;
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
public class Enrollment extends BaseEntity {

    @ManyToOne
    @JoinColumn(name = "student_id")
    StudentReference student;

    @ManyToOne
    @JoinColumn(name = "course_id")
    Course course;

    @ManyToOne
    @JoinColumn(name = "semester_id")
    Semester semester;
}
