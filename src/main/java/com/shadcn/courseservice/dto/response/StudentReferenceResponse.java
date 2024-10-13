package com.shadcn.courseservice.dto.response;

import java.util.List;

import jakarta.persistence.*;

import lombok.*;
import lombok.experimental.FieldDefaults;
import lombok.experimental.SuperBuilder;

@Data
@AllArgsConstructor
@NoArgsConstructor
@ToString
@SuperBuilder
@FieldDefaults(level = AccessLevel.PRIVATE)
public class StudentReferenceResponse {
    String name;
    Long studentId;
    DepartmentResponse department;
    AcademicYearResponse academicYear;
    List<EnrollmentResponse> enrollments;
    List<LessonResponse> lessons;
}
