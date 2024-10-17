package com.shadcn.courseservice.dto.response;

import java.time.LocalDate;
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
public class SemesterResponse {
    String name;
    LocalDate startDate;
    LocalDate endDate;
    AcademicYearResponse academicYear;
    List<CourseResponse> courses;
}
