package com.shadcn.courseservice.controller;

import static com.shadcn.courseservice.constant.PathConstant.API_V1_BUILDINGS;

import java.util.List;

import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import com.shadcn.courseservice.dto.request.building.RoomCreationRequest;
import com.shadcn.courseservice.dto.request.building.RoomUpdateRequest;
import com.shadcn.courseservice.dto.response.ApiResponse;
import com.shadcn.courseservice.dto.response.PageResponse;
import com.shadcn.courseservice.dto.response.building.RoomResponse;
import com.shadcn.courseservice.entity.Room;
import com.shadcn.courseservice.service.IRoomService;

import lombok.RequiredArgsConstructor;

@RestController
@RequiredArgsConstructor
@RequestMapping(API_V1_BUILDINGS)
@PreAuthorize("hasRole('ADMIN')")
public class RoomController {

    private final IRoomService roomService;

    @PostMapping("/{buildingId}/rooms")
    public ApiResponse<Room> addRoom(@PathVariable Long buildingId, @RequestBody RoomCreationRequest request) {
        Room room = roomService.addRoom(buildingId, request);
        return ApiResponse.success(room);
    }

    @PutMapping("/rooms/{roomId}")
    public ApiResponse<Room> updateRoom(RoomUpdateRequest request, @PathVariable Long roomId) {
        Room room = roomService.updateRoom(request, roomId);
        return ApiResponse.success(room);
    }

    @DeleteMapping("/rooms")
    public ApiResponse<Void> deleteRoom(@RequestBody List<Long> roomIds) {
        roomService.deleteRoom(roomIds);
        return ApiResponse.empty();
    }

    @GetMapping("/rooms/{roomId}")
    public ApiResponse<RoomResponse> getRoomById(@PathVariable Long roomId) {
        RoomResponse room = roomService.getRoomById(roomId);
        return ApiResponse.success(room);
    }

    @GetMapping("/{buildingId}/rooms")
    public ApiResponse<PageResponse<RoomResponse>> getListRoomByBuilding(
            @PathVariable Long buildingId, @RequestParam Integer current, @RequestParam Integer pageSize) {
        PageResponse<RoomResponse> rooms = roomService.getListRoomByBuilding(buildingId, current, pageSize);
        return ApiResponse.success(rooms);
    }
}
