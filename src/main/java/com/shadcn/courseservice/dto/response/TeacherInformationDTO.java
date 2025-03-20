package com.shadcn.courseservice.dto.response;

import lombok.*;
import lombok.experimental.FieldDefaults;
import lombok.experimental.SuperBuilder;

@Data
@AllArgsConstructor
@NoArgsConstructor
@ToString
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
public class TeacherInformationDTO {
    String id;
    String teacherId;
    String firstName;
    String middleName;
    String lastName;
    String email;
    String phoneNumber;
    String avatarPath;
 
}
