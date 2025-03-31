package com.shadcn.courseservice.dto.response;

import java.time.LocalDate;
import java.util.List;

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
public class SemesterResponse {
    String id;
    String name;
    LocalDate startDate;
    LocalDate endDate;
    AcademicYearResponse academicYear;
    List<CourseResponse> courses;
    boolean registrationOpen;
    boolean semesterActive;
    LocalDate registrationStartDate;
    LocalDate registrationEndDate;
}
