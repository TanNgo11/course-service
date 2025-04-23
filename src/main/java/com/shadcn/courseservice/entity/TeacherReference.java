package com.shadcn.courseservice.entity;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

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
public class TeacherReference extends BaseEntity {

    Long teacherId;

    String contactLink;

    String officeLocation;

    String officeHours;

    String otherInformation;

    String username;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "department_id")
    Department department;

    @ManyToMany(fetch = FetchType.LAZY)
    @JoinTable(
            name = "course_teacher",
            joinColumns = @JoinColumn(name = "teacher_id"),
            inverseJoinColumns = @JoinColumn(name = "course_id"))
    List<Course> courses = new ArrayList<>();

    @ElementCollection
    @CollectionTable(name = "teacher_available_slots", joinColumns = @JoinColumn(name = "teacher_reference_id"))
    @Column(name = "time_slot_id")
    Set<Long> availableTimeSlots;

    @OneToMany(mappedBy = "teacherReference", cascade = CascadeType.ALL, orphanRemoval = true)
    Set<TeacherCourseRole> teacherCourseRoles;
    
}
