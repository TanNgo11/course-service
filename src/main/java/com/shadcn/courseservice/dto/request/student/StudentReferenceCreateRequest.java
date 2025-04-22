package com.shadcn.courseservice.dto.request.student;

import lombok.*;
import lombok.experimental.FieldDefaults;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
public class StudentReferenceCreateRequest {
    Long studentId;
    Long academicYearId;
    Long departmentId;
    String name;
}
