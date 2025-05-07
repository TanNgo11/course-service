package com.shadcn.courseservice.dto.request.attendance;

import com.shadcn.courseservice.enums.AttendanceStatus;

import lombok.*;
import lombok.experimental.FieldDefaults;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class StudentAttendanceRecord {

    Long studentId;

    AttendanceStatus status;

    String notes;
}
