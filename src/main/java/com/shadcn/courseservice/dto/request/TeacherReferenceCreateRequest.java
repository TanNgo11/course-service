package com.shadcn.courseservice.dto.request;

import lombok.*;
import lombok.experimental.FieldDefaults;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
public class TeacherReferenceCreateRequest {
    Long teacherId;
    Long courseId;
    Long departmentId;
}
