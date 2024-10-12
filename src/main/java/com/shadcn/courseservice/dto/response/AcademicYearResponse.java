package com.shadcn.courseservice.dto.response;

import java.time.LocalDate;
import java.util.List;

import com.shadcn.courseservice.entity.Department;
import com.shadcn.courseservice.entity.Semester;

import lombok.*;
import lombok.experimental.FieldDefaults;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
public class AcademicYearResponse {
    LocalDate startYear;
    LocalDate endYear;
    List<DepartmentResponse> departments;
    List<SemesterResponse> semesters;
}
