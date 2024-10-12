package com.shadcn.courseservice.dto.response;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.shadcn.courseservice.entity.AcademicYear;
import com.shadcn.courseservice.entity.Subject;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToMany;
import jakarta.persistence.ManyToOne;
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
public class SemesterResponse {
    String name;
    LocalDate startDate;
    LocalDate endDate;
    AcademicYearResponse academicYear;
    List<SubjectResponse> subjects;
}
