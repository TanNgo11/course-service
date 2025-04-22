package com.shadcn.courseservice.service;

import java.util.List;

import com.shadcn.courseservice.dto.request.building.RoomCreationRequest;
import com.shadcn.courseservice.dto.request.building.RoomUpdateRequest;
import com.shadcn.courseservice.dto.response.PageResponse;
import com.shadcn.courseservice.dto.response.building.RoomResponse;
import com.shadcn.courseservice.entity.Room;

public interface IRoomService {
    Room addRoom(Long buildingId, RoomCreationRequest request);

    Room updateRoom(RoomUpdateRequest request, Long roomId);

    PageResponse<RoomResponse> getListRoomByBuilding(Long buildingId, Integer current, Integer pageSize);

    void deleteRoom(List<Long> roomIds);

    RoomResponse getRoomById(Long roomId);
}
