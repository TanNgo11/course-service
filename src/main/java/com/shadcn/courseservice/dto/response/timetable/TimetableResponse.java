package com.shadcn.courseservice.dto.response.timetable;

import java.time.DayOfWeek;
import java.util.List;
import java.util.Set;

import com.shadcn.courseservice.dto.response.classSession.ClassSessionResponse;
import com.shadcn.courseservice.dto.response.course.CourseResponse;

import lombok.*;
import lombok.experimental.FieldDefaults;

@Data
@AllArgsConstructor
@NoArgsConstructor
@ToString
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
public class TimetableResponse {
    Long id;

    CourseResponse course;

    Set<DayOfWeek> daysOfWeek;

    List<ClassSessionResponse> classSessions;
}
