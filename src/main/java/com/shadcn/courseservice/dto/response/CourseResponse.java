package com.shadcn.courseservice.dto.response;

import java.util.List;

import lombok.*;
import lombok.experimental.FieldDefaults;
import lombok.experimental.SuperBuilder;

@Data
@AllArgsConstructor
@NoArgsConstructor
@ToString
@SuperBuilder
@FieldDefaults(level = AccessLevel.PRIVATE)
public class CourseResponse {
    String name;
    List<DepartmentResponse> departments;
    List<LessonResponse> lessons;
    List<SemesterResponse> semesters;
    List<EnrollmentResponse> enrollments;
    List<TeacherReferenceResponse> teacherReferences;
}
