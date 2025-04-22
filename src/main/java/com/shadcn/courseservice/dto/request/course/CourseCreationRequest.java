package com.shadcn.courseservice.dto.request.course;

import java.time.LocalDate;
import java.util.List;

import com.shadcn.courseservice.enums.CourseStatus;

import lombok.*;
import lombok.experimental.FieldDefaults;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
public class CourseCreationRequest {
    private Long baseCourseId;
    private List<String> studentIds;
    private List<String> studentUsernames;
    private List<String> teacherIds;
    private List<String> teacherUsernames;
    private Long semesterId; // IDs of associated semesters
    private LocalDate startDate;
    private LocalDate endDate;
    private CourseStatus processStatus;
}
