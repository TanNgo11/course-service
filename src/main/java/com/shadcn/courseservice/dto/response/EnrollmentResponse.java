package com.shadcn.courseservice.dto.response;

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
    SubjectResponse subject;
    SemesterResponse semester;
}
