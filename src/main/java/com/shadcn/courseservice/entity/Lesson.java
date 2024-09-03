package com.shadcn.courseservice.entity;

import java.util.List;

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
public class Lesson extends BaseEntity {

    String title;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "subject_id")
    Subject subject;

    @ManyToMany(fetch = FetchType.LAZY)
    @JoinTable(
            name = "lesson_enrollment",
            joinColumns = @JoinColumn(name = "lesson_id"),
            inverseJoinColumns = @JoinColumn(name = "student_id"))
    List<StudentReference> students;
}
