package com.shadcn.courseservice.dto.request.semester;

import java.time.LocalDate;
import java.util.List;

import jakarta.persistence.*;

import com.shadcn.courseservice.entity.AcademicYear;
import com.shadcn.courseservice.entity.Course;
import com.shadcn.courseservice.entity.Registration;

import lombok.*;
import lombok.experimental.FieldDefaults;

@Builder
@AllArgsConstructor
@NoArgsConstructor
@Data
@FieldDefaults(level = AccessLevel.PRIVATE)
public class SemesterCreationRequest {
    String name;
    LocalDate startDate;
    LocalDate endDate;
    AcademicYear academicYear;
    List<Course> courses;
    List<Registration> registrations;
    LocalDate registrationStartDate;
    LocalDate registrationEndDate;
    boolean semesterActive;
    boolean registrationOpen;
}
