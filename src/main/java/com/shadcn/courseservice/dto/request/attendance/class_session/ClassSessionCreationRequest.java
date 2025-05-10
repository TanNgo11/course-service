package com.shadcn.courseservice.dto.request.attendance.class_session;

import java.time.LocalDate;

import com.shadcn.courseservice.enums.ClassSessionStatus;
import com.shadcn.courseservice.enums.ClassSessionType;

import lombok.*;
import lombok.experimental.FieldDefaults;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class ClassSessionCreationRequest {
    Long timetableId;
    Long roomId;
    Long teacherId;
    Long timeSlotId;

    LocalDate sessionDate;
    Integer weekNumber;

    ClassSessionType sessionType;

    String notes;
    boolean isException;

    ClassSessionStatus status;

    // Optional field to indicate if this session replaces another
    Long replacedById;
}
