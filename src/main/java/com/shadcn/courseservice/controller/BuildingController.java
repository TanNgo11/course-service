package com.shadcn.courseservice.controller;

import static com.shadcn.courseservice.constant.PathConstant.API_V1_BUILDINGS;

import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import com.shadcn.courseservice.dto.request.building.BuildingCreationRequest;
import com.shadcn.courseservice.dto.request.building.BuildingUpdateRequest;
import com.shadcn.courseservice.dto.response.ApiResponse;
import com.shadcn.courseservice.dto.response.PageResponse;
import com.shadcn.courseservice.dto.response.building.BuildingResponse;
import com.shadcn.courseservice.entity.Building;
import com.shadcn.courseservice.service.IBuildingService;

import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@RestController
@RequestMapping(API_V1_BUILDINGS)
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@PreAuthorize("hasRole('ADMIN')")
public class BuildingController {

    IBuildingService buildingService;

    @PostMapping()
    ApiResponse<Void> createBuilding(@RequestBody BuildingCreationRequest request) {
        buildingService.createBuilding(request);
        return ApiResponse.empty();
    }

    @GetMapping()
    ApiResponse<PageResponse<BuildingResponse>> getAllBuildings(
            @RequestParam(defaultValue = "1") int current, @RequestParam(defaultValue = "10") int pageSize) {
        PageResponse<BuildingResponse> buildings = buildingService.getAllBuildings(current, pageSize);

        log.info(buildings.toString());

        return ApiResponse.success(buildings);
    }

    @GetMapping("/{id}")
    ApiResponse<BuildingResponse> getBuildingById(@PathVariable Long id) {
        BuildingResponse building = buildingService.getBuildingById(id);
        return ApiResponse.success(building);
    }

    @PutMapping("/{id}")
    ApiResponse<Building> updateBuilding(@PathVariable Long id, @RequestBody BuildingUpdateRequest request) {
        Building building = buildingService.updateBuilding(id, request);
        return ApiResponse.success(building);
    }

    @DeleteMapping("/{id}")
    ApiResponse<Void> deleteBuilding(@PathVariable Long id) {
        buildingService.deleteBuilding(id);
        return ApiResponse.empty();
    }
}
