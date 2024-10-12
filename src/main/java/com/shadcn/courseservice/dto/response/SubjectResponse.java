package com.shadcn.courseservice.dto.response;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.shadcn.courseservice.entity.*;
import jakarta.persistence.*;
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
public class SubjectResponse {
    String name;
    List<DepartmentResponse> departments;
    List<LessonResponse> lessons;
    List<SemesterResponse> semesters;
    List<EnrollmentResponse> enrollments;
    List<TeacherReferenceResponse> teacherReferences;
}
