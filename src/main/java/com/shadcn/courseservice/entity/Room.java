package com.shadcn.courseservice.entity;

import jakarta.persistence.*;

import com.shadcn.courseservice.enums.RoomStatus;
import com.shadcn.courseservice.enums.RoomType;

import lombok.*;
import lombok.experimental.FieldDefaults;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
@Entity
public class Room extends BaseEntity {

    @Column(unique = true)
    String code;

    String name;

    int capacity;

    @Enumerated(EnumType.STRING)
    RoomType roomType;

    @Enumerated(EnumType.STRING)
    RoomStatus status;

    @ManyToOne
    @JoinColumn(name = "building_id", nullable = false)
    Building building;
}
