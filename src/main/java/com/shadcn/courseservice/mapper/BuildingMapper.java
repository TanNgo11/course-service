package com.shadcn.courseservice.mapper;

import java.util.List;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import com.shadcn.courseservice.dto.response.building.BuildingResponse;
import com.shadcn.courseservice.entity.Building;

@Mapper(
        componentModel = "spring",
        uses = {RoomMapper.class})
public interface BuildingMapper {

    // @Mapping(target = "departmentName", source = "department.departmentName")
    @Mapping(target = "rooms", source = "rooms")
    BuildingResponse toBuildingResponse(Building building);

    // @Mapping(target = "departmentName", source = "department.departmentName")
    List<BuildingResponse> toBuildingResponseList(List<Building> buildings);
}
