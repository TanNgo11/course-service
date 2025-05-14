package com.shadcn.courseservice.dto.response.attendance.class_session;

import java.time.LocalDate;
import java.util.List;

import com.shadcn.courseservice.dto.response.attendance.AttendanceResponse;
import com.shadcn.courseservice.entity.TimeSlot;
import com.shadcn.courseservice.entity.Timetable;

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
public class ClassSessionResponse {
    Long id;
    Timetable timetable;
    Long roomId;
    Long teacherId;
    TimeSlot timeSlot;

    LocalDate sessionDate;
    Integer weekNumber;

    String sessionType;

    String notes;
    boolean isException;

    String status;

    // Optional field to indicate if this session replaces another
    Long replacedById;

    // Optional field to indicate if this session has been replaced by another
    Long replacedBySessionId;

    List<AttendanceResponse> attendances;
}
