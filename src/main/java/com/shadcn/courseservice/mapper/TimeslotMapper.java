package com.shadcn.courseservice.mapper;

import java.util.List;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Mappings;

import com.shadcn.courseservice.dto.response.timeslot.TimeslotResponse;
import com.shadcn.courseservice.entity.TimeSlot;

@Mapper(componentModel = "spring")
public interface TimeslotMapper {

    @Mappings({@Mapping(target = "id", source = "timeSlot.id")})
    TimeslotResponse toTimeslotResponse(TimeSlot timeSlot);

    List<TimeslotResponse> toTimeslotResponseList(List<TimeSlot> lessons);
}
