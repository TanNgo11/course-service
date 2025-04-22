package com.shadcn.courseservice.service;

import com.shadcn.courseservice.dto.request.building.BuildingCreationRequest;
import com.shadcn.courseservice.dto.request.building.BuildingUpdateRequest;
import com.shadcn.courseservice.dto.response.PageResponse;
import com.shadcn.courseservice.dto.response.building.BuildingResponse;
import com.shadcn.courseservice.entity.Building;

public interface IBuildingService {

    void createBuilding(BuildingCreationRequest request);

    Building updateBuilding(Long id, BuildingUpdateRequest request);

    void deleteBuilding(Long id);

    BuildingResponse getBuildingById(Long id);

    PageResponse<BuildingResponse> getAllBuildings(int current, int pageSize);
}
