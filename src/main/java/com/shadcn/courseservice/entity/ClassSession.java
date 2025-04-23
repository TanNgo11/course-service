package com.shadcn.courseservice.entity;

import java.time.LocalDate;
import java.time.LocalTime;

import jakarta.persistence.*;

import com.shadcn.courseservice.enums.ClassSessionStatus;
import com.shadcn.courseservice.enums.ClassSessionType;

import lombok.*;
import lombok.experimental.FieldDefaults;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
@Entity
public class ClassSession extends BaseEntity {

    @ManyToOne
    @JoinColumn(name = "timetable_id", nullable = false)
    Timetable timetable;

    @ManyToOne
    @JoinColumn(name = "room_id", nullable = false)
    Room room;

    @ManyToOne
    @JoinColumn(name = "teacher_reference_id", nullable = false)
    TeacherReference teacher;

    @ManyToOne
    @JoinColumn(name = "time_slot_id", nullable = false)
    TimeSlot timeSlot;

    LocalDate sessionDate;

    @Column(nullable = true)
    Integer weekNumber;

    @Enumerated(EnumType.STRING)
    ClassSessionType sessionType;

    String notes;

    boolean isException;

    @Enumerated(EnumType.STRING)
    ClassSessionStatus status;

    @OneToOne
    @JoinColumn(name = "replaced_by_id")
    ClassSession replacedBy;
}
