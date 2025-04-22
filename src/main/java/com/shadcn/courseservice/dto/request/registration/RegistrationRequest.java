package com.shadcn.courseservice.dto.request.registration;

import java.util.List;

import lombok.*;
import lombok.experimental.FieldDefaults;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
public class RegistrationRequest {
    long studentId;
    List<Long> courseIds;
    long semesterId;
}
