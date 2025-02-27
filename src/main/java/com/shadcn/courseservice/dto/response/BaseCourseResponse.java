package com.shadcn.courseservice.dto.response;

import java.util.Collection;

import lombok.*;
import lombok.experimental.FieldDefaults;
import lombok.experimental.SuperBuilder;

@Data
@AllArgsConstructor
@NoArgsConstructor
@ToString
@SuperBuilder
@FieldDefaults(level = AccessLevel.PRIVATE)
public class BaseCourseResponse {
    String id;
    String code;
    String name;
    String imageUri;
    String description;
    String credit;
    String status;
    Collection<BaseCourseResponse> requiredBaseCourses;
}
