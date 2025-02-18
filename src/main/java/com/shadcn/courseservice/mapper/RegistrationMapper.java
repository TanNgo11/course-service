package com.shadcn.courseservice.mapper;

import java.util.List;

import org.mapstruct.Mapper;

import com.shadcn.courseservice.dto.response.RegistrationResponse;
import com.shadcn.courseservice.entity.Registration;

@Mapper(componentModel = "spring")
public interface RegistrationMapper {
    RegistrationResponse toRegistrationResponse(Registration registration);

    List<RegistrationResponse> toRegistrationResponseList(List<Registration> registrations);
}
