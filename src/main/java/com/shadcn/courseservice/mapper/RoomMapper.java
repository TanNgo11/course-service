package com.shadcn.courseservice.mapper;

import com.shadcn.courseservice.dto.response.building.RoomResponse;
import com.shadcn.courseservice.entity.Room;
import org.mapstruct.Mapper;

import java.util.List;

@Mapper(componentModel = "spring")
public interface RoomMapper {
    List<RoomResponse> toRoomResponse(List<Room> room);
}
