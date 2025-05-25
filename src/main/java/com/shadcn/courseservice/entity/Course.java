package com.shadcn.courseservice.entity;

import java.time.LocalDate;
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

    @Column(columnDefinition = "TEXT")
    String courseInformation;

    @Column(columnDefinition = "TEXT")
    String assessmentPlan;

    @Column(columnDefinition = "TEXT")
    String learningMaterialsAndOutcomes;

    //    @ManyToMany(mappedBy = "courses", fetch = FetchType.LAZY)
    //    @JsonBackReference
    //    List<Department> departments;

    @OneToMany(mappedBy = "course", fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    List<Lesson> lessons;

    @ManyToOne
    @JoinColumn(name = "semester_id", nullable = false)
    @JsonIgnore
    Semester semester;

    @ManyToMany(mappedBy = "courses", cascade = CascadeType.ALL)
    @JsonIgnore
    List<StudentReference> studentReferences;

    @ManyToMany(mappedBy = "courses", cascade = CascadeType.ALL)
    @JsonIgnore
    List<TeacherReference> teacherReferences;

    @OneToMany(mappedBy = "course", fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    @JsonIgnore
    List<CourseFile> files;

    @OneToMany(mappedBy = "course", cascade = CascadeType.ALL, orphanRemoval = true)
    @JsonManagedReference
    List<Registration> registrations;

    @OneToMany(mappedBy = "course", fetch = FetchType.LAZY, cascade = CascadeType.ALL, orphanRemoval = true)
    List<Timetable> timetables;

    LocalDate startDate;

    LocalDate endDate;

    int remain;

    int maxStudents;

    int numsOfTimetable;

    Integer theorySessionsPerWeek;

    Integer practiceSessionsPerWeek;

    CourseStatus processStatus;
}
