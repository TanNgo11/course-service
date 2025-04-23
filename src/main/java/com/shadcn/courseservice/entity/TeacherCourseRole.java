package com.shadcn.courseservice.entity;

import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.FieldDefaults;

import java.util.Set;

import com.shadcn.courseservice.enums.TeacherRole;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
@Entity
@Table(name = "teacher_course_role")
public class TeacherCourseRole extends BaseEntity {

    @ManyToOne
    @JoinColumn(name = "teacher_reference_id", nullable = false)
    TeacherReference teacherReference;

    @ManyToOne
    @JoinColumn(name = "course_id", nullable = false)
    Course course;

    @ElementCollection
    @CollectionTable(name = "teacher_course_role_types", joinColumns = @JoinColumn(name = "teacher_course_role_id"))
    @Enumerated(EnumType.STRING)
    @Column(name = "role")
    Set<TeacherRole> roles;
}