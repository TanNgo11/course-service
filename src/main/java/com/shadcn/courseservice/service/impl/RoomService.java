package com.shadcn.courseservice.service.impl;

import com.shadcn.courseservice.dto.response.PageResponse;
import com.shadcn.courseservice.dto.response.building.RoomResponse;
import com.shadcn.courseservice.entity.Building;
import com.shadcn.courseservice.entity.Department;
import com.shadcn.courseservice.entity.Room;
import com.shadcn.courseservice.entity.Semester;
import com.shadcn.courseservice.enums.RoomStatus;
import com.shadcn.courseservice.enums.RoomType;
import com.shadcn.courseservice.exception.AppException;
import com.shadcn.courseservice.exception.ErrorCode;
import com.shadcn.courseservice.mapper.RoomMapper;
import com.shadcn.courseservice.repository.BuildingRepository;
import com.shadcn.courseservice.repository.DepartmentRepository;
import com.shadcn.courseservice.repository.RoomRepository;
import com.shadcn.courseservice.service.IRoomService;
import com.shadcn.courseservice.util.ConverToPaginationResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class RoomService implements IRoomService {

    private final RoomRepository roomRepository;
    private final BuildingRepository buildingRepository;
    private final DepartmentRepository departmentRepository;
    private final RoomMapper roomMapper;

    @Override
    @Transactional
    public Room addRoom(Long buildingId, String code, String name, int capacity, RoomType roomType) {
        // Check if building exists
        Building building = buildingRepository.findById(buildingId)
                .orElseThrow(() -> new AppException(ErrorCode.BUILDING_NOT_FOUND));

        // Check if room code already exists
        if (roomRepository.existsByCode(code)) {
            throw new AppException(ErrorCode.ROOM_CODE_ALREADY_EXISTS);
        }

        // Create and save the room
        Room room = Room.builder()
                .code(code)
                .name(name)
                .capacity(capacity)
                .roomType(roomType)
                .status(RoomStatus.AVAILABLE)
                .building(building)
                .build();

        return roomRepository.save(room);
    }

    @Override
    @Transactional
    public Room updateRoom(Long roomId, String name, Integer capacity, RoomType roomType) {
        // Find the room
        Room room = roomRepository.findById(roomId)
                .orElseThrow(() -> new AppException(ErrorCode.ROOM_NOT_FOUND));

        // Update fields if provided
        if (name != null) {
            room.setName(name);
        }
        
        if (capacity != null) {
            room.setCapacity(capacity);
        }
        
        if (roomType != null) {
            room.setRoomType(roomType);
        }

        // Save and return the updated room
        return roomRepository.save(room);
    }

    @Override
    @Transactional(readOnly = true)
    public List<RoomResponse> getListRoomByDepartment(Long departmentId) {
        Department department = departmentRepository.findById(departmentId)
                .orElseThrow(() -> new AppException(ErrorCode.DEPARTMENT_NOT_FOUND));

        List<Building> allBuildingsInDepartment = department.getBuildings();

        List<Room> allRooms = new ArrayList<>();
        for (Building building : allBuildingsInDepartment) {
            allRooms.addAll(roomRepository.findByBuildingId(building.getId()));
        }

        return roomMapper.toRoomResponse(allRooms);
    }
}