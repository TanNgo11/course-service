package com.shadcn.courseservice.entity;

import java.time.DayOfWeek;
import java.util.List;
import java.util.Set;

import jakarta.persistence.*;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonIgnore;

import lombok.*;
import lombok.experimental.FieldDefaults;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
@Entity
public class Timetable extends BaseEntity {

    @ManyToOne
    @JoinColumn(name = "course_id", nullable = false)
    @JsonIgnore
    Course course;

    @ElementCollection
    @Enumerated(EnumType.STRING)
    @CollectionTable(name = "timetable_days", joinColumns = @JoinColumn(name = "timetable_id"))
    @Column(name = "day_of_week")
    Set<DayOfWeek> daysOfWeek;

    @OneToMany(mappedBy = "timetable", fetch = FetchType.LAZY, cascade = CascadeType.ALL, orphanRemoval = true)
    @JsonBackReference
    List<ClassSession> classSessions;

    @ManyToMany
    @JoinTable(
            name = "timetable_student",
            joinColumns = @JoinColumn(name = "timetable_id"),
            inverseJoinColumns = @JoinColumn(name = "student_reference_id"))
    List<StudentReference> students;
}
