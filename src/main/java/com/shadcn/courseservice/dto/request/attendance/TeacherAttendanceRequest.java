package com.shadcn.courseservice.dto.request.attendance;

import java.util.List;

import com.shadcn.courseservice.entity.StudentAttendanceRecord;

import lombok.*;
import lombok.experimental.FieldDefaults;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class TeacherAttendanceRequest {

    Long classSessionId;

    List<StudentAttendanceRecord> attendanceRecords;
}
