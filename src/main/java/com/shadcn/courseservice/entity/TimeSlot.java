package com.shadcn.courseservice.entity;

import java.time.LocalTime;
import java.time.DayOfWeek;

import jakarta.persistence.*;

import lombok.*;
import lombok.experimental.FieldDefaults;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
@Entity
public class TimeSlot extends BaseEntity {

    @Enumerated(EnumType.STRING)
    DayOfWeek dayOfWeek; 

    LocalTime startTime; 

    LocalTime endTime; 
}