package com.shadcn.courseservice.entity;

import jakarta.persistence.*;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.shadcn.courseservice.enums.AttendanceStatus;

import lombok.*;
import lombok.experimental.FieldDefaults;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
@Entity
public class Attendance extends BaseEntity {

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "class_session_id", nullable = false)
    @JsonBackReference
    ClassSession classSession;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "student_reference_id", nullable = false)
    @JsonBackReference
    StudentReference student;

    @Enumerated(EnumType.STRING)
    AttendanceStatus status;

    String notes;
}
