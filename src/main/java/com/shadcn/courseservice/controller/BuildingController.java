package com.shadcn.courseservice.controller;

import com.shadcn.courseservice.dto.request.building.BuildingCreationRequest;
import com.shadcn.courseservice.dto.response.ApiResponse;
import com.shadcn.courseservice.service.IBuildingService;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import static com.shadcn.courseservice.constant.PathConstant.API_V1_BUILDINGS;

@RestController
@RequestMapping(API_V1_BUILDINGS)
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class BuildingController {

    IBuildingService buildingService;

    @PostMapping()
    @PreAuthorize("hasRole('ADMIN')")
    ApiResponse<Void> createBuilding(@RequestBody BuildingCreationRequest request) {
        buildingService.createBuilding(request);
        return ApiResponse.empty();
    }


}
