package com.shadcn.courseservice.mapper;

import java.util.List;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import com.shadcn.courseservice.dto.response.building.RoomResponse;
import com.shadcn.courseservice.entity.Room;

@Mapper(
        componentModel = "spring",
        uses = {BuildingMapper.class})
public interface RoomMapper {
    @Mapping(target = "buildingCode", source = "building.code")
    List<RoomResponse> toRoomResponse(List<Room> room);

    @Mapping(target = "buildingCode", source = "building.code")
    RoomResponse toRoomResponse(Room room);
}
