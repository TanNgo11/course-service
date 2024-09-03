package com.shadcn.courseservice.mapper;

import org.mapstruct.Mapper;

import com.shadcn.courseservice.dto.response.BaseResponse;
import com.shadcn.courseservice.entity.BaseEntity;

@Mapper(componentModel = "spring")
public interface BaseMapper {
    BaseResponse baseEntityToBaseDTO(BaseEntity baseEntity);
}
