package com.shadcn.courseservice.dto.request.registration;

import com.shadcn.courseservice.enums.TeacherRole;
import lombok.*;
import lombok.experimental.FieldDefaults;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
public class RegistrationTeacherRoleRequest {
    Long teacherId;
    Long courseId;
    TeacherRole teacherRole;
}
