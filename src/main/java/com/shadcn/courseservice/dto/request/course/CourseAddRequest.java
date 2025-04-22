package com.shadcn.courseservice.dto.request.course;

import java.util.List;

import lombok.*;
import lombok.experimental.FieldDefaults;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
public class CourseAddRequest {
    String departmentId;
    String courseId;
    List<String> studentIds;
    List<String> teacherIds;
    List<String> semesterIds;
}
