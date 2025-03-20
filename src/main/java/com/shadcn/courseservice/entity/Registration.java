package com.shadcn.courseservice.entity;

import java.time.LocalDate;

import jakarta.persistence.*;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.shadcn.courseservice.enums.RegistrationStatus;

import lombok.*;
import lombok.experimental.FieldDefaults;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
@Entity
public class Registration extends BaseEntity {

    @ManyToOne
    @JoinColumn(name = "student_reference_id")
    StudentReference studentReference;

    @ManyToOne
    @JsonBackReference
    @JoinColumn(name = "course_id")
    Course course;

    @Enumerated(EnumType.STRING)
    RegistrationStatus status;

    @ManyToOne
    @JsonBackReference
    @JoinColumn(name = "semester_id")
    Semester semester;

    LocalDate registrationDate;

    LocalDate cancellationDeadline;
}
