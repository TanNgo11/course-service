package com.shadcn.courseservice.entity;

import java.util.List;

import jakarta.persistence.*;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import com.shadcn.courseservice.enums.BaseCourseStatus;

import lombok.*;
import lombok.experimental.FieldDefaults;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
@Entity
public class BaseCourse extends BaseEntity {
    @Column(unique = true)
    String code;

    String name;

    String imageUri;

    String description;

    Long credit;

    @Enumerated(EnumType.STRING)
    BaseCourseStatus status;

    //    @ManyToMany
    //    @JoinTable(
    //            name = "department_basecourse",
    //            joinColumns = @JoinColumn(name = "base_course_id"),
    //            inverseJoinColumns = @JoinColumn(name = "department_id"))
    //    private List<Department> departments;

    @OneToMany(mappedBy = "baseCourse", fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    @JsonManagedReference
    List<Course> courses;

    @ManyToMany(mappedBy = "baseCourses", fetch = FetchType.LAZY)
    @JsonBackReference
    List<Department> departments;
}
