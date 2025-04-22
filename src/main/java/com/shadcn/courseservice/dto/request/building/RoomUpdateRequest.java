package com.shadcn.courseservice.dto.request.building;

import com.shadcn.courseservice.enums.RoomType;

import lombok.*;
import lombok.experimental.FieldDefaults;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
public class RoomUpdateRequest {
    String code;
    String name;
    int capacity;
    RoomType roomType;
}
