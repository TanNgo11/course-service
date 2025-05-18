package com.shadcn.courseservice.mapper;

import com.shadcn.courseservice.dto.response.timeslot.TimeslotResponse;
import com.shadcn.courseservice.entity.TimeSlot;
import org.mapstruct.Mapper;

import java.util.List;

@Mapper(componentModel = "spring")
public interface TimeslotMapper {

    TimeslotResponse toTimeslotResponse(TimeSlot timeSlot);

    List<TimeslotResponse> toTimeslotResponseList(List<TimeSlot> lessons);
}
