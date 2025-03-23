package com.shadcn.courseservice.dto.request.course;

import lombok.*;
import lombok.experimental.FieldDefaults;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
public class UpdateCourseInformationRequest {
    String courseInformation;
    String assessmentPlan;
    String learningMaterialsAndOutcomes;
}
