package com.shadcn.courseservice.dto.response;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.shadcn.courseservice.entity.AcademicYear;
import com.shadcn.courseservice.entity.Department;
import com.shadcn.courseservice.entity.Enrollment;
import com.shadcn.courseservice.entity.Lesson;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.*;
import lombok.experimental.FieldDefaults;
import lombok.experimental.SuperBuilder;

import java.time.LocalDate;
import java.util.List;

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
