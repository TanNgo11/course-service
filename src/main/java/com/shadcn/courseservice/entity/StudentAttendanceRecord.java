package com.shadcn.courseservice.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.Entity;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;

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
public class StudentAttendanceRecord extends BaseEntity {
    Long studentId;

    AttendanceStatus status;

    String notes;

    Long classSessionId;

    @ManyToOne()
    @JoinColumn(name = "classSession_id")
    @JsonIgnore
    ClassSession classSession;
}
