package com.shadcn.courseservice.dto.response.attendance;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.experimental.SuperBuilder;

@Data
@SuperBuilder
@NoArgsConstructor
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class AttendanceResponse {
    Long studentId;
    Long studentReferenceId;
    String studentName;
    String classSessionId;
    String status;
    String notes;
    String date;
}
