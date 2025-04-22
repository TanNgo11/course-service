package com.shadcn.courseservice.service;

import com.shadcn.courseservice.dto.response.PageResponse;
import com.shadcn.courseservice.dto.response.building.RoomResponse;
import com.shadcn.courseservice.entity.Room;
import com.shadcn.courseservice.enums.RoomType;

import java.util.List;

public interface IRoomService {
    Room addRoom(Long buildingId, String code, String name, int capacity, RoomType roomType);

    Room updateRoom(Long roomId, String name, Integer capacity, RoomType roomType);

    List<RoomResponse> getListRoomByDepartment(Long departmentId);
}