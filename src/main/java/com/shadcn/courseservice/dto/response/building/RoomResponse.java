package com.shadcn.courseservice.dto.response.building;

import com.shadcn.courseservice.dto.response.BaseResponse;
import jakarta.persistence.*;

import com.shadcn.courseservice.enums.RoomStatus;
import com.shadcn.courseservice.enums.RoomType;

import lombok.*;
import lombok.experimental.FieldDefaults;
import lombok.experimental.SuperBuilder;

@Data
@AllArgsConstructor
@NoArgsConstructor
@ToString
@SuperBuilder
@FieldDefaults(level = AccessLevel.PRIVATE)
public class RoomResponse  {
    Long id;
    String code;
    String name;
    int capacity;
    RoomType roomType;
    RoomStatus status;
    String buildingCode;
}
