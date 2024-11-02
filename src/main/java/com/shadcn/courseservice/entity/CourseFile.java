package com.shadcn.courseservice.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.ManyToOne;
import lombok.*;
import lombok.experimental.FieldDefaults;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
@Entity
public class CourseFile extends BaseEntity {
    private String name;
    private String url;
    private String type;

    @ManyToOne
    private Course course;
}
