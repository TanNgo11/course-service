package com.shadcn.courseservice.dto.request;

import java.time.LocalDate;
import java.util.List;

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

    List<Long> departmentIds;
    List<Long> semesterIds;
}
