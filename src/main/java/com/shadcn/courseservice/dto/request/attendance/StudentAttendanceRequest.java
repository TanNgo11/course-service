package com.shadcn.courseservice.dto.request.attendance;

import lombok.*;
import lombok.experimental.FieldDefaults;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class StudentAttendanceRequest {

    Long studentId;

    Long classSessionId;

    String notes;
}
