package com.shadcn.courseservice.dto.response;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.shadcn.courseservice.entity.Semester;
import com.shadcn.courseservice.entity.StudentReference;
import com.shadcn.courseservice.entity.Subject;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import lombok.*;
import lombok.experimental.FieldDefaults;
import lombok.experimental.SuperBuilder;

import java.time.LocalDate;

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
