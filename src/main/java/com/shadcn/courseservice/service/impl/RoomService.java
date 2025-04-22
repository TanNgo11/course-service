package com.shadcn.courseservice.service.impl;

import java.util.List;

import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.shadcn.courseservice.dto.request.building.RoomCreationRequest;
import com.shadcn.courseservice.dto.request.building.RoomUpdateRequest;
import com.shadcn.courseservice.dto.response.PageResponse;
import com.shadcn.courseservice.dto.response.building.RoomResponse;
import com.shadcn.courseservice.entity.Building;
import com.shadcn.courseservice.entity.Room;
import com.shadcn.courseservice.enums.RoomStatus;
import com.shadcn.courseservice.exception.AppException;
import com.shadcn.courseservice.exception.ErrorCode;
import com.shadcn.courseservice.mapper.RoomMapper;
import com.shadcn.courseservice.repository.BuildingRepository;
import com.shadcn.courseservice.repository.DepartmentRepository;
import com.shadcn.courseservice.repository.RoomRepository;
import com.shadcn.courseservice.service.IRoomService;
import com.shadcn.courseservice.util.ConverToPaginationResponse;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class RoomService implements IRoomService {

    private final RoomRepository roomRepository;
    private final BuildingRepository buildingRepository;
    private final DepartmentRepository departmentRepository;
    private final RoomMapper roomMapper;

    @Override
    @Transactional
    public Room addRoom(Long buildingId, RoomCreationRequest request) {
        Building building = buildingRepository
                .findById(buildingId)
                .orElseThrow(() -> new AppException(ErrorCode.BUILDING_NOT_FOUND));

        if (roomRepository.existsByCode(request.getCode())) {
            throw new AppException(ErrorCode.ROOM_CODE_ALREADY_EXISTS);
        }

        Room room = Room.builder()
                .code(request.getCode())
                .name(request.getName())
                .capacity(request.getCapacity())
                .roomType(request.getRoomType())
                .status(RoomStatus.AVAILABLE)
                .building(building)
                .build();

        return roomRepository.save(room);
    }

    @Override
    @Transactional
    public Room updateRoom(RoomUpdateRequest request, Long roomId) {
        Room room = roomRepository.findById(roomId).orElseThrow(() -> new AppException(ErrorCode.ROOM_NOT_FOUND));

        if (request.getName() != null) {
            room.setName(request.getName());
        }

        if (request.getCapacity() != room.getCapacity()) {
            room.setCapacity(request.getCapacity());
        }

        if (request.getRoomType() != null) {
            room.setRoomType(request.getRoomType());
        }

        if (request.getCode() != null && !roomRepository.existsByCode(request.getCode())) {
            room.setCode(request.getCode());
        } else if (request.getCode() != null) {
            throw new AppException(ErrorCode.ROOM_CODE_ALREADY_EXISTS);
        }

        return roomRepository.save(room);
    }

    @Override
    public PageResponse<RoomResponse> getListRoomByBuilding(Long buildingId, Integer current, Integer pageSize) {
        List<Room> rooms = roomRepository.findByBuildingId(buildingId);
        Pageable pageable = PageRequest.of(current - 1, pageSize);
        PageImpl<Room> roomPage = new PageImpl<>(rooms, pageable, rooms.size());
        return ConverToPaginationResponse.toPageResponse(roomPage, roomMapper::toRoomResponse, current);
    }

    @Override
    public void deleteRoom(List<Long> roomIds) {
        List<Room> rooms = roomRepository.findAllById(roomIds);
        roomRepository.deleteAll(rooms);
        roomRepository.flush();
    }

    @Override
    public RoomResponse getRoomById(Long roomId) {
        Room room = roomRepository.findById(roomId).orElseThrow(() -> new AppException(ErrorCode.ROOM_NOT_FOUND));
        return roomMapper.toRoomResponse(room);
    }
}
