package com.shadcn.courseservice.service.impl;

import com.shadcn.courseservice.dto.request.building.BuildingCreationRequest;
import com.shadcn.courseservice.entity.Building;
import com.shadcn.courseservice.entity.Room;
import com.shadcn.courseservice.enums.RoomStatus;
import com.shadcn.courseservice.enums.RoomType;
import com.shadcn.courseservice.exception.AppException;
import com.shadcn.courseservice.exception.ErrorCode;
import com.shadcn.courseservice.repository.BuildingRepository;
import com.shadcn.courseservice.service.IBuildingService;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class BuildingService implements IBuildingService {

    BuildingRepository buildingRepository;

    @Override
    @Transactional
    public void createBuilding(BuildingCreationRequest request) {
        var code = request.getCode();
        var name = request.getName();
        var initNumberOfRooms = request.getInitNumberOfRooms();
        var initCapacityOfEachRoom = request.getInitCapacityOfEachRoom();
        boolean existsByCode = buildingRepository.existsByCode(code);
        if (existsByCode) {
            throw new AppException(ErrorCode.BUILDING_CODE_ALREADY_EXISTS);
        }

        Building building = Building.builder().code(code).name(name).build();

        List<Room> rooms = new ArrayList<>();
        for (int i = 1; i <= initNumberOfRooms; i++) {
            String roomCode = String.format("%s-%03d", code, i);
            String roomName = String.format("Room %03d", i);

            Room room = Room.builder()
                    .code(roomCode)
                    .name(roomName)
                    .capacity(initCapacityOfEachRoom)
                    .roomType(RoomType.LECTURE)
                    .status(RoomStatus.AVAILABLE)
                    .capacity(initCapacityOfEachRoom)
                    .building(building)
                    .build();

            rooms.add(room);
        }

        building.setRooms(rooms);

        buildingRepository.save(building);
    }
}
