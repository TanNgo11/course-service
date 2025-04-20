package com.shadcn.courseservice.dto.request.building;

import lombok.*;
import lombok.experimental.FieldDefaults;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
public class BuildingCreationRequest {
    String code;
    String name;
    Integer initNumberOfRooms;
    Integer initCapacityOfEachRoom;
}
