package com.shadcn.courseservice.dto.response;

import java.time.LocalDate;

import com.shadcn.courseservice.entity.BaseCourse;
import com.shadcn.courseservice.entity.Course;
import com.shadcn.courseservice.enums.RegistrationStatus;

import lombok.*;
import lombok.experimental.FieldDefaults;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
public class RegistrationResponse {
    String studentId;
    String courseCode;
    String courseName;
    String courseDescription;
    RegistrationStatus status;
    String semesterName;
    String semesterId;
    LocalDate registrationDate;
    LocalDate cancellationDeadline;
    String thumbnail;
    Course courseDetails;
    BaseCourse baseCourseDetails;
}
