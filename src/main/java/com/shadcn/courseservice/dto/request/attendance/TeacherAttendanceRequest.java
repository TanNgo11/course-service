package com.shadcn.courseservice.dto.request.attendance;

import java.util.List;

import com.shadcn.courseservice.dto.response.attendance.AttendanceResponse;

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

    List<AttendanceResponse> attendanceResponses;
}
