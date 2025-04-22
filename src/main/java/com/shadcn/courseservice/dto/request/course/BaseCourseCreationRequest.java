package com.shadcn.courseservice.dto.request.course;

import jakarta.persistence.*;

import com.shadcn.courseservice.enums.BaseCourseStatus;

import lombok.*;
import lombok.experimental.FieldDefaults;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
public class BaseCourseCreationRequest {
    String code;
    String name;
    String imageUri;
    String description;
    Long credit;

    @Enumerated(EnumType.STRING)
    BaseCourseStatus status;

    // List<Course> courses;
}
