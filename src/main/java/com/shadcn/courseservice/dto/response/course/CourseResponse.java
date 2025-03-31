package com.shadcn.courseservice.dto.response.course;

import java.util.List;

import com.shadcn.courseservice.dto.response.DepartmentResponse;
import com.shadcn.courseservice.dto.response.TeacherInformationDTO;

import lombok.*;
import lombok.experimental.FieldDefaults;
import lombok.experimental.SuperBuilder;

@Data
@AllArgsConstructor
@NoArgsConstructor
@ToString
@SuperBuilder
@FieldDefaults(level = AccessLevel.PRIVATE)
public class CourseResponse {
    String id;
    String name;
    String imageUri;
    String code;
    String credit;
    String description;
    String startTime;
    String endTime;
    String startDate;
    String endDate;
    int remain;
    String courseInformation;
    String assessmentPlan;
    String learningMaterialsAndOutcomes;

    TeacherInformationDTO teacher;
    List<String> studentIds;
    List<DepartmentResponse> departments;
    //    List<LessonResponse> lessons;
    //    List<SemesterResponse> semesters;
    //    List<EnrollmentResponse> enrollments;
    //    List<TeacherReferenceResponse> teacherReferences;
}
