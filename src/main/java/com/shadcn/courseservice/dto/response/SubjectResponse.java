package com.shadcn.courseservice.dto.response;

import java.util.List;

import jakarta.persistence.*;

import com.shadcn.courseservice.entity.*;

import lombok.*;
import lombok.experimental.FieldDefaults;
import lombok.experimental.SuperBuilder;

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
