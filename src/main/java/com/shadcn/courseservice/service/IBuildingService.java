package com.shadcn.courseservice.service;

import com.shadcn.courseservice.dto.request.building.BuildingCreationRequest;

public interface IBuildingService {

    void createBuilding(BuildingCreationRequest request);
}
