package com.shadcn.courseservice.entity;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import com.shadcn.courseservice.enums.ClassSessionStatus;
import com.shadcn.courseservice.enums.ClassSessionType;
import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.FieldDefaults;

import java.time.LocalDate;
import java.util.List;

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
    @JsonManagedReference
    Timetable timetable;

    @ManyToOne
    @JoinColumn(name = "room_id", nullable = false)
    @JsonIgnore
    Room room;

    @ManyToOne
    @JoinColumn(name = "teacher_reference_id", nullable = false)
            @JsonIgnore
    TeacherReference teacher;

    @ManyToOne
    @JoinColumn(name = "time_slot_id", nullable = false)
    @JsonBackReference
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
    @JsonIgnore
    ClassSession replacedBy;

    @OneToMany(mappedBy = "classSession", fetch = FetchType.LAZY, cascade = CascadeType.ALL, orphanRemoval = true)
    @JsonManagedReference
    List<Attendance> attendances;
}
