package com.shadcn.courseservice.dto.response.classSession;

import java.time.LocalDate;

import com.shadcn.courseservice.dto.response.building.RoomResponse;
import com.shadcn.courseservice.dto.response.teacher.TeacherInformationDTO;
import com.shadcn.courseservice.dto.response.timeslot.TimeslotResponse;
import com.shadcn.courseservice.enums.ClassSessionType;

import lombok.*;
import lombok.experimental.FieldDefaults;

@Data
@AllArgsConstructor
@NoArgsConstructor
@ToString
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
public class ClassSessionResponse {
    Long id;

    RoomResponse room;

    LocalDate sessionDate;

    TeacherInformationDTO teacher;

    TimeslotResponse timeSlot;

    Integer weekNumber;

    ClassSessionType sessionType;

    String notes;

    boolean isException;

    ClassSessionResponse replacedBy;
}
