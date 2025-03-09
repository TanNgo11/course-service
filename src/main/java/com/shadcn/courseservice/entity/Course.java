package com.shadcn.courseservice.entity;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;

import jakarta.persistence.*;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import com.shadcn.courseservice.enums.CourseStatus;

import lombok.*;
import lombok.experimental.FieldDefaults;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
@Entity
public class Course extends BaseEntity {
    @ManyToOne
    @JoinColumn(name = "base_course_id", nullable = false)
    @JsonBackReference
    BaseCourse baseCourse;

    String imageUri;

    @ElementCollection
    List<String> studentIds;

    @ElementCollection
    List<String> studentUsernames;

    @ElementCollection
    List<String> teacherIds;

    @ElementCollection
    List<String> teacherUsernames;

    //    @ManyToMany(mappedBy = "courses", fetch = FetchType.LAZY)
    //    @JsonBackReference
    //    List<Department> departments;

    @OneToMany(mappedBy = "course", fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    @JsonIgnore
    List<Lesson> lessons;

    @ManyToOne
    @JoinColumn(name = "semester_id", nullable = false)
    @JsonIgnore
    Semester semester;

    @ManyToMany(mappedBy = "courses", fetch = FetchType.LAZY)
    @JsonIgnore
    List<TeacherReference> teacherReferences;

    @OneToMany(mappedBy = "course", fetch = FetchType.LAZY)
    @JsonIgnore
    List<CourseFile> files;

    @OneToMany(mappedBy = "course", cascade = CascadeType.ALL, orphanRemoval = true)
    @JsonManagedReference
    List<Registration> registrations;

    LocalTime startTime;

    LocalTime endTime;

    LocalDate startDate;

    LocalDate endDate;

    int remain;

    CourseStatus processStatus;
}
