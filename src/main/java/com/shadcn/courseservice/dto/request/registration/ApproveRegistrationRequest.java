package com.shadcn.courseservice.dto.request.registration;

import java.util.List;

import lombok.*;
import lombok.experimental.FieldDefaults;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
public class ApproveRegistrationRequest {
    long semesterId;
    List<Long> registrationIds;
}
