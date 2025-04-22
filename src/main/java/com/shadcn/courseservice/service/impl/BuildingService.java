package com.shadcn.courseservice.service.impl;

import java.util.ArrayList;
import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.shadcn.courseservice.dto.request.building.BuildingCreationRequest;
import com.shadcn.courseservice.dto.request.building.BuildingUpdateRequest;
import com.shadcn.courseservice.dto.response.PageResponse;
import com.shadcn.courseservice.dto.response.building.BuildingResponse;
import com.shadcn.courseservice.entity.Building;
import com.shadcn.courseservice.entity.Room;
import com.shadcn.courseservice.enums.RoomStatus;
import com.shadcn.courseservice.enums.RoomType;
import com.shadcn.courseservice.exception.AppException;
import com.shadcn.courseservice.exception.ErrorCode;
import com.shadcn.courseservice.mapper.BuildingMapper;
import com.shadcn.courseservice.repository.BuildingRepository;
import com.shadcn.courseservice.repository.DepartmentRepository;
import com.shadcn.courseservice.service.IBuildingService;
import com.shadcn.courseservice.util.ConverToPaginationResponse;

import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;

@Service
@RequiredArgsConstructor
@Slf4j
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class BuildingService implements IBuildingService {

    BuildingRepository buildingRepository;
    BuildingMapper buildingMapper;
    DepartmentRepository departmentRepository;

    @Override
    @Transactional
    public void createBuilding(BuildingCreationRequest request) {
        var code = request.getCode();
        var name = request.getName();
        var defaultNumberOfRooms = request.getDefaultNumberOfRooms();
        var defaultCapacityOfRooms = request.getDefaultCapacityOfRooms();
        boolean existsByCode = buildingRepository.existsByCode(code);
        if (existsByCode) {
            throw new AppException(ErrorCode.BUILDING_CODE_ALREADY_EXISTS);
        }

        // Department department = departmentRepository.findDepartmentById(request.getDepartmentId());

        Building building = Building.builder().code(code).name(name).build();

        List<Room> rooms = new ArrayList<>();
        for (int i = 1; i <= defaultNumberOfRooms; i++) {
            // Generate room code and name
            String roomCode = String.format("%s-%03d", code, i);
            String roomName = String.format("Room %03d", i);

            Room room = Room.builder()
                    .code(roomCode)
                    .name(roomName)
                    .capacity(defaultCapacityOfRooms)
                    .roomType(RoomType.LECTURE)
                    .status(RoomStatus.AVAILABLE)
                    .building(building)
                    .build();

            rooms.add(room);
        }

        building.setRooms(rooms);

        buildingRepository.save(building);
    }

    @Override
    @Transactional
    public Building updateBuilding(Long id, BuildingUpdateRequest request) {
        Building building =
                buildingRepository.findById(id).orElseThrow(() -> new AppException(ErrorCode.BUILDING_NOT_FOUND));

        if (!building.getCode().equals(request.getCode()) && buildingRepository.existsByCode(request.getCode())) {
            throw new AppException(ErrorCode.BUILDING_CODE_ALREADY_EXISTS);
        }

        building.setCode(request.getCode());
        building.setName(request.getName());

        return buildingRepository.save(building);
    }

    @Override
    @Transactional
    public void deleteBuilding(Long id) {
        Building building =
                buildingRepository.findById(id).orElseThrow(() -> new AppException(ErrorCode.BUILDING_NOT_FOUND));

        buildingRepository.delete(building);
    }

    @Override
    public BuildingResponse getBuildingById(Long id) {
        Building building =
                buildingRepository.findById(id).orElseThrow(() -> new AppException(ErrorCode.BUILDING_NOT_FOUND));

        return buildingMapper.toBuildingResponse(building);
    }

    @Override
    public PageResponse<BuildingResponse> getAllBuildings(int current, int pageSize) {
        Pageable pageable = PageRequest.of(current - 1, pageSize);
        Page<Building> buildings = buildingRepository.findAll(pageable);

        log.info("Building list size: {}", buildings.getTotalElements());

        return ConverToPaginationResponse.toPageResponse(buildings, buildingMapper::toBuildingResponse, current);
    }
}
