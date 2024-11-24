package com.shadcn.courseservice.mapper;

import java.util.List;

import org.mapstruct.Mapper;

import com.shadcn.courseservice.dto.response.DepartmentResponse;
import com.shadcn.courseservice.entity.Department;

@Mapper(componentModel = "spring")
public interface DepartmentMapper {

    DepartmentResponse toDepartmentResponse(Department department);

    List<DepartmentResponse> toListDepartmentResponse(List<Department> departments);
}
