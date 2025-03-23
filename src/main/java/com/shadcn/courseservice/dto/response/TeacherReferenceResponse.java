package com.shadcn.courseservice.dto.response;

import java.util.List;

import com.shadcn.courseservice.dto.response.course.CourseResponse;
import lombok.*;
import lombok.experimental.FieldDefaults;
import lombok.experimental.SuperBuilder;

@Data
@AllArgsConstructor
@NoArgsConstructor
@ToString
@SuperBuilder
@FieldDefaults(level = AccessLevel.PRIVATE)
public class TeacherReferenceResponse {
    Long teacherId;
    CourseResponse course;
    DepartmentResponse department;
    List<CourseResponse> courses;
}
