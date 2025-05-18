package com.shadcn.courseservice.dto.request.timeslot;

import lombok.*;
import lombok.experimental.FieldDefaults;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
public class DeleteTimeslotRequest {
    Long teacherId;
    Long timeSlotId;
}
