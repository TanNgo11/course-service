package com.shadcn.courseservice.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;
import org.mapstruct.NullValuePropertyMappingStrategy;

import com.shadcn.courseservice.dto.request.teacher.UpdateTeacherReferenceRequest;
import com.shadcn.courseservice.dto.response.TeacherInformationDTO;
import com.shadcn.courseservice.dto.response.TeacherProfileResponse;
import com.shadcn.courseservice.entity.TeacherReference;

@Mapper(componentModel = "spring", nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
public interface TeacherMapper {

    @Mapping(target = "id", source = "profile.id")
    @Mapping(target = "teacherId", source = "profile.teacherId")
    @Mapping(target = "firstName", source = "profile.firstName")
    @Mapping(target = "middleName", source = "profile.middleName")
    @Mapping(target = "lastName", source = "profile.lastName")
    @Mapping(target = "email", source = "profile.email")
    @Mapping(target = "phoneNumber", source = "profile.phoneNumber")
    @Mapping(target = "avatarPath", source = "profile.avatarPath")
    @Mapping(target = "contactLink", source = "reference.contactLink")
    @Mapping(target = "officeLocation", source = "reference.officeLocation")
    @Mapping(target = "officeHours", source = "reference.officeHours")
    @Mapping(target = "otherInformation", source = "reference.otherInformation")
    TeacherInformationDTO toTeacherInfo(TeacherProfileResponse profile, TeacherReference reference);

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "teacherId", ignore = true)
    @Mapping(target = "department", ignore = true)
    @Mapping(target = "courses", ignore = true)
    void updateTeacherReference(
            @MappingTarget TeacherReference teacherReference,
            UpdateTeacherReferenceRequest updateTeacherReferenceRequest);
}
