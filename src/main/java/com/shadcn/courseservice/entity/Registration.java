package com.shadcn.courseservice.entity;

import java.time.LocalDate;

import jakarta.persistence.*;

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
    StudentProfile studentProfile;

    @ManyToOne
    Course course;

    @Enumerated(EnumType.STRING)
    RegistrationStatus status;

    @ManyToOne
    @JoinColumn(name = "semester_id")
    Semester semester;

    LocalDate registrationDate;

    LocalDate cancellationDeadline; // Allow cancellation until this date
}
