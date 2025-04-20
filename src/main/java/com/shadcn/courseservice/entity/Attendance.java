package com.shadcn.courseservice.entity;

import jakarta.persistence.*;

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

    @ManyToOne
    @JoinColumn(name = "class_session_id", nullable = false)
    ClassSession classSession;

    @ManyToOne
    @JoinColumn(name = "student_reference_id", nullable = false)
    StudentReference student;

    @Enumerated(EnumType.STRING)
    AttendanceStatus status;

    String notes;
}
