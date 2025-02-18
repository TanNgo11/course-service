package com.shadcn.courseservice.dto.response;

import lombok.*;
import lombok.experimental.FieldDefaults;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
public class RegistrationResponse {
    long id;
    long studentId;
    long courseId;
    long semesterId;
    boolean registrationOpen;
}
