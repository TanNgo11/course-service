package com.shadcn.courseservice.dto.response;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.shadcn.courseservice.entity.Department;
import com.shadcn.courseservice.entity.Subject;
import jakarta.persistence.*;
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
public class TeacherReferenceResponse {
    Long teacherId;
    SubjectResponse subject;
    DepartmentResponse department;
    List<SubjectResponse> subjects;
}
