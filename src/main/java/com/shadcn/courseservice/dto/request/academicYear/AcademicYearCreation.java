package com.shadcn.courseservice.dto.request.academicYear;

import java.time.LocalDate;

import com.fasterxml.jackson.annotation.JsonFormat;

import lombok.*;
import lombok.experimental.FieldDefaults;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
public class AcademicYearCreation {
    @JsonFormat(pattern = "dd-MM-yyyy")
    LocalDate startYear;

    @JsonFormat(pattern = "dd-MM-yyyy")
    LocalDate endYear;
}
