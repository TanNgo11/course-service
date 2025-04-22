package com.shadcn.courseservice.dto.response.registration;

import com.shadcn.courseservice.dto.response.academicYear.SemesterResponse;
import com.shadcn.courseservice.dto.response.course.CourseResponse;
import com.shadcn.courseservice.dto.response.student.StudentReferenceResponse;

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
