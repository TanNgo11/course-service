package com.shadcn.courseservice.dto.response.student;

import com.shadcn.courseservice.dto.response.academicYear.AcademicYearResponse;
import com.shadcn.courseservice.dto.response.building.DepartmentResponse;

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
    //    List<EnrollmentResponse> enrollments;
    //    List<LessonResponse> lessons;
}
