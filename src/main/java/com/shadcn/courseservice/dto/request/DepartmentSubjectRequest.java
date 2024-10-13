package com.shadcn.courseservice.dto.request;

import lombok.*;
import lombok.experimental.FieldDefaults;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
public class DepartmentSubjectRequest {
    private Long departmentId;
    private List<Long> subjectIds;
}
