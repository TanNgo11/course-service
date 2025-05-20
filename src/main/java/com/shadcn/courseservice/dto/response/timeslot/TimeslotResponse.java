package com.shadcn.courseservice.dto.response.timeslot;

import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.LocalTime;

import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;

import lombok.*;
import lombok.experimental.FieldDefaults;

@Data
@AllArgsConstructor
@NoArgsConstructor
@ToString
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
public class TimeslotResponse {
    Long id;

    LocalDate date;

    @Enumerated(EnumType.STRING)
    DayOfWeek dayOfWeek;

    LocalTime startTime;

    LocalTime endTime;
}
