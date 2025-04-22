package com.shadcn.courseservice.dto.request.course;

import java.util.List;

import lombok.*;
import lombok.experimental.FieldDefaults;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
public class AssignTeacherRequest {
    long teacherId;
    List<Long> courseIds;
    long semesterId;
    long departmentId;
    String username;
}
