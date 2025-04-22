package com.shadcn.courseservice.controller;

import com.shadcn.courseservice.dto.response.ApiResponse;
import com.shadcn.courseservice.dto.response.PageResponse;
import com.shadcn.courseservice.dto.response.building.RoomResponse;
import com.shadcn.courseservice.entity.Room;
import com.shadcn.courseservice.enums.RoomType;
import com.shadcn.courseservice.service.IRoomService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

import static com.shadcn.courseservice.constant.PathConstant.API_V1_BUILDINGS;

@RestController
@RequiredArgsConstructor
@RequestMapping(API_V1_BUILDINGS)
public class RoomController {

    private final IRoomService roomService;

    @PostMapping("/{buildingId}/rooms")
    public ApiResponse<Room> addRoom(
            @PathVariable Long buildingId,
            @RequestParam String code,
            @RequestParam String name,
            @RequestParam int capacity,
            @RequestParam RoomType roomType) {

        Room room = roomService.addRoom(buildingId, code, name, capacity, roomType);
        return ApiResponse.success(room);
    }

    @PutMapping("/rooms/{roomId}")
    public ApiResponse<Room> updateRoom(
            @PathVariable Long roomId,
            @RequestParam(required = false) String name,
            @RequestParam(required = false) Integer capacity,
            @RequestParam(required = false) RoomType roomType) {

        Room room = roomService.updateRoom(roomId, name, capacity, roomType);
        return ApiResponse.success(room);
    }

    @GetMapping("/departments/{departmentId}/rooms")
    public ApiResponse<PageResponse<RoomResponse>> getListByDepartment(@PathVariable Long departmentId) {
        List<RoomResponse> rooms = roomService.getListRoomByDepartment(departmentId);
        PageResponse<RoomResponse> pageResponse = new PageResponse<>();
        pageResponse.setData(rooms);
        pageResponse.setTotalElements(rooms.size());
        pageResponse.setPageSize(rooms.size());

        return ApiResponse.success(pageResponse);
    }
}
