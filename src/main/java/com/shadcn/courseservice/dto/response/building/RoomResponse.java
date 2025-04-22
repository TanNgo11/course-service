package com.shadcn.courseservice.dto.response.building;

import com.shadcn.courseservice.entity.Building;
import com.shadcn.courseservice.enums.RoomStatus;
import com.shadcn.courseservice.enums.RoomType;
import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.FieldDefaults;
import lombok.experimental.SuperBuilder;

@Data
@AllArgsConstructor
@NoArgsConstructor
@ToString
@SuperBuilder
@FieldDefaults(level = AccessLevel.PRIVATE)
public class RoomResponse {
    String code;
    String name;
    int capacity;
    RoomType roomType;
    RoomStatus status;
    Building building;
}
