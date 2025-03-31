package com.shadcn.courseservice.dto.response;

import com.shadcn.courseservice.dto.response.course.CourseResponse;

import lombok.*;
import lombok.experimental.FieldDefaults;
import lombok.experimental.SuperBuilder;

@Data
@AllArgsConstructor
@NoArgsConstructor
@ToString
@SuperBuilder
@FieldDefaults(level = AccessLevel.PRIVATE)
public class EnrollmentResponse {
    StudentReferenceResponse student;
    CourseResponse course;
    SemesterResponse semester;
}
